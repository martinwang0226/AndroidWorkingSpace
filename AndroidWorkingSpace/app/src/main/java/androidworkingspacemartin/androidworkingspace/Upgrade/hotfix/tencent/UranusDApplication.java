package androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.tencent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.HotFix;
import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.util.load.AmigoService;
import androidworkingspacemartin.androidworkingspace.Upgrade.manager.Uranus;

/**
 * Created by martinwang on 2017/6/21.
 */

public abstract class UranusDApplication extends DefaultApplicationLike {

    protected static Context sContext;

    public static Context getApplicationContext() {
        return sContext;
    }

    public static final String TAG = "Tinker.DApplication";

    public UranusDApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
//        Mu.install(base);
        // 安装tinker
        Beta.installTinker(this);

        TinkerManager.TinkerPatchResultListener patchResultListener = new TinkerManager.TinkerPatchResultListener() {
            @Override
            public void onPatchResult(PatchResult result) {
                // you can get the patch result 补丁应用成功
                if (Uranus.isDebug) {
                    Log.i(Uranus.TAG, "onPatchResult -> " + result.toString());
                }
                if (result.isSuccess) {
                    HotFix.setIsPatchReady(true);
                }
                if (HotFix.isForceRestart()) {
                    if (Uranus.isDebug) {
                        Log.i(Uranus.TAG, "修复成功,直接强制重启 onPatchResult -> ForceRestart");
                    }

                    AlarmManager mgr = (AlarmManager) sContext.getSystemService(Context.ALARM_SERVICE);
                    try {
                        Intent intent = new Intent(sContext, fristResumeActivity.getClass());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent restartIntent = PendingIntent.getActivity(sContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
                    } catch (Exception e) {
                        Log.e(TAG, "first class error:" + e);
                    }
                    AmigoService.restartMainProcess(getApplicationContext());
                }
            }
        };
        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this, null, null, null, patchResultListener, null);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    public abstract String getMainProcessName();

    public abstract String getCheckOrderProcessName();

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplication();
        String processName = getProcessName(sContext);
//        configTinker();
        Log.e("application","============>进程名:"+processName);
        /**
         * 非当前进程，防止Application create多次调用
         */
        if (!TextUtils.isEmpty(processName) && processName.equals(getMainProcessName())) {
            _onCreate();
        } else if (!TextUtils.isEmpty(processName) && processName.equals(getCheckOrderProcessName())) {
            _onCheckOrderCreate();
        }
    }

    public abstract void _onCreate();

    public abstract void _onCheckOrderCreate();

    protected String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public interface OnFrontBackListener {
        void onFrontBackChange(boolean isBackGround, long lastTime, long keepTotalTime);
    }

    private ArrayList<OnFrontBackListener> mFrontBackListener;

    public void registerFrontBackListener(OnFrontBackListener callback) {
        if (mFrontBackListener == null) {
            mFrontBackListener = new ArrayList<>();
            activityLifecycleCallback();
        }
        synchronized (mFrontBackListener) {
            mFrontBackListener.add(callback);
        }
    }

    public void unRegisterFrontBackListener(OnFrontBackListener callback) {
        if (mFrontBackListener == null) {
            return;
        }
        synchronized (mFrontBackListener) {
            mFrontBackListener.remove(callback);
        }
    }

    private static long lastBackTime = 0;

    void dispatchFrontBack(boolean isBackGround) {
        if (mFrontBackListener == null || mFrontBackListener.isEmpty()) {
            return;
        }
        long nowTime = System.currentTimeMillis();
        long totalTime = lastBackTime > 0 ? nowTime - lastBackTime : 0;
        for (int i = 0; i < mFrontBackListener.size(); i++) {
            mFrontBackListener.get(i).onFrontBackChange(isBackGround, nowTime, totalTime);
        }
        lastBackTime = nowTime;
    }

    public static boolean isBackGroud() {
        return sIsBackGround;
    }

    static boolean sIsBackGround = true;

    static int sActivityCount = 0;

    Activity fristResumeActivity;
    Activity lastResumeActivity;

    public void activityLifecycleCallback() {
        registerActivityLifecycleCallback(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                if(fristResumeActivity == null) {
                    fristResumeActivity = activity;
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                sActivityCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                lastResumeActivity = activity;
                if (sIsBackGround) {
                    sIsBackGround = false;
                    if (Uranus.isDebug) {
                        Log.i(Uranus.TAG, "APP回到了前台咯");
                    }
                    dispatchFrontBack(sIsBackGround);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                sActivityCount--;
                if (sActivityCount <= 0) {
                    sActivityCount = 0;
                    sIsBackGround = true;
                    if (Uranus.isDebug) {
                        Log.i(Uranus.TAG, "APP遁入后台");
                    }
                    dispatchFrontBack(sIsBackGround);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void configTinker() {
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true;
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true;
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = true;
        // 补丁回调接口
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFile) {
                Toast.makeText(sContext, "补丁下载地址" + patchFile, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Toast.makeText(sContext, String.format(Locale.getDefault(), "%s %d%%", Beta.strNotificationDownloading, (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadSuccess(String msg) {
                Toast.makeText(sContext, "补丁下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadFailure(String msg) {
                Toast.makeText(sContext, "补丁下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplySuccess(String msg) {
                Toast.makeText(sContext, "补丁应用成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplyFailure(String msg) {
                Toast.makeText(sContext, "补丁应用失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPatchRollback() {

            }
        };

        // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
        Bugly.setIsDevelopmentDevice(sContext, false);
        // 多渠道需求塞入
        // String channel = WalleChannelReader.getChannel(getApplication());
        // Bugly.setAppChannel(getApplication(), channel);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        Bugly.init(sContext, "e9d0b7f57f", true);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
//        Beta.unInit();
//        if (level == Application.TRIM_MEMORY_UI_HIDDEN) {
//            sIsBackGround = true;
//            if (Uranus.isDebug) {
//                Log.i(Uranus.TAG, "APP遁入后台");
//            }
//            dispatchFrontBack(sIsBackGround);
//        }
    }
}
