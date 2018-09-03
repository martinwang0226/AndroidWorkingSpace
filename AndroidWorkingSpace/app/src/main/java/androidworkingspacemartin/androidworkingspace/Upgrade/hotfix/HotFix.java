package androidworkingspacemartin.androidworkingspace.Upgrade.hotfix;

import android.content.Context;
import android.util.Log;

import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.ali.AliFix;
import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.tencent.TinkerFix;
import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.tencent.UranusDApplication;
import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.util.load.AmigoService;
import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.util.pactchQuery.PollingService;
import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.util.pactchQuery.PollingUtils;
import androidworkingspacemartin.androidworkingspace.Upgrade.manager.Uranus;
import androidworkingspacemartin.androidworkingspace.Upgrade.manager.UranusBuilder;


/**
 * Created by martinwang on 2017/6/12.
 */

public class HotFix {

    static UranusBuilder sUranusBuilder;

    public static UranusBuilder getUranusBuilder() {
        return sUranusBuilder == null ? new UranusBuilder.Builder().build() : sUranusBuilder;
    }

    public static void init(Context context, UranusBuilder uranusBuilder) {

        sUranusBuilder = uranusBuilder;

        isForceRestart = uranusBuilder.isForceRestart;

        if (sUranusBuilder.hotfixEnable) {
            /**
             * 阿里 hotfix
             * @see http://baichuan.taobao.com/docs/doc.htm?spm=a3c0d.7629140.0.0.LwMYuI&treeId=234&articleId=106531&docType=1
             */
            AliFix.getInstance().init(context, sUranusBuilder.channel);
        }

        if (sUranusBuilder.tinkerEnable) {
            /**
             * 腾讯 fix
             */
            TinkerFix.getInstance().init();
            /**
             * @see https://bugly.qq.com/docs/user-guide/faq-android-hotfix/?v=20170512172046
             * bugly 开发设备 热修复
             */
            TinkerFix.getInstance().setIsDevelopmentDevice(context, sUranusBuilder.isDevelopmentDevice);
        }

        if (sUranusBuilder.queryNow) {
            queryNewPatch();
        }

        if (sUranusBuilder.uranusDApplication != null) {
            if (Uranus.isDebug) {
                Log.i(Uranus.TAG, "Hofix注册 前后台切换监听");
            }
            sUranusBuilder.uranusDApplication.registerFrontBackListener(new UranusDApplication.OnFrontBackListener() {
                @Override
                public void onFrontBackChange(boolean isBackGround, long lastTime, long keepTotalTime) {
                    if (isBackGround) {
                        if(isPatchReady()) {
                            if (Uranus.isDebug) {
                                Log.i(Uranus.TAG, "周期性检测到成功应用新补丁，并等待时机重启（退到后台 或 锁屏）");
                            }
                            AmigoService.restartMainProcess(sUranusBuilder.uranusDApplication.getApplicationContext());
                        }
                    } else {
                        if(sUranusBuilder.isAutoQuery) {
                            autoQueryNewPatch(keepTotalTime);
                        }
                    }
                }
            });
        }
    }

    private static final long PATCH_QUERY_INTERVAL = 30000;

    public static void startAutoPollPatchService(Context context) {
        PollingUtils.startPollingService(context, 60 * 5, PollingService.class, PollingService.ACTION);
    }

    /**
     * 自动主动获取
     * 请求补丁时机
     * 1.打开app
     * 2.后台切回前台且间隔时间大于30s
     * 3.收到主动请求补丁推送
     */
    private static void autoQueryNewPatch(long keepTotalTime) {
        /**
         * 后台切回前台且间隔时间大于30s，主动查询一次补丁
         */
        if (keepTotalTime > PATCH_QUERY_INTERVAL) {
            if (Uranus.isDebug) {
                Log.i(Uranus.TAG, "周期性查询新补丁");
            }
            queryNewPatch();
        }
    }

    /**
     * 查询新补丁
     */
    public static void queryNewPatch() {
        if (getUranusBuilder().hotfixEnable) {
            AliFix.getInstance().query();
        }
        if (getUranusBuilder().tinkerEnable) {
            TinkerFix.getInstance().query();
        }
    }

    /**
     * 清理旧补丁
     */
    public static void cleanPatches() {
        if (Uranus.isDebug) {
            Log.i(Uranus.TAG, "cleanPatches");
        }
        if (getUranusBuilder().hotfixEnable) {
            AliFix.getInstance().clean();
        }
        if (getUranusBuilder().tinkerEnable) {
            TinkerFix.getInstance().clean();
        }
    }

    public static boolean isPatchReady() {
        return isPatchReady;
    }

    public static void setIsPatchReady(boolean isPatchReady) {
        HotFix.isPatchReady = isPatchReady;
    }

    static boolean isPatchReady = false;

    public static boolean isForceRestart() {
        return isForceRestart;
    }

    public static void setIsForceRestart(boolean isForceRestart) {
        HotFix.isForceRestart = isForceRestart;
    }

    static boolean isForceRestart = false;
}

