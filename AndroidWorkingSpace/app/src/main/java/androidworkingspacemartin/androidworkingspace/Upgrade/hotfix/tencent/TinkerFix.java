package androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.tencent;

import android.content.Context;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;

import java.util.Locale;

import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.BaseFix;
import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.HotFix;
import androidworkingspacemartin.androidworkingspace.Upgrade.manager.Uranus;

/**
 * Created by martinwang on 2017/6/12.
 */

public class TinkerFix extends BaseFix {

    private TinkerFix() { }

    public static TinkerFix getInstance() {
        return LazyHolder.strategy;
    }

    private static class LazyHolder {
        public static final TinkerFix strategy = new TinkerFix();
    }

    @Override
    public void init() {
        initBuglyHotfixConfig();
    }

    /**
     * @see https://bugly.qq.com/docs/user-guide/api-hotfix/?v=20170504092424
     */
    private static void initBuglyHotfixConfig() {

        /**
         * 关闭热更新能力
         * 升级SDK默认是开启热更新能力的，如果你不需要使用热更新，可以将这个接口设置为false。
         */
        Beta.enableHotfix = true;

        /**
         * 设置是否允许自动下载补丁
         * 默认为true，如果想选择下载补丁的时机，设置为false即可。
         */
        Beta.canAutoDownloadPatch = true;

        /**
         * 设置是否允许自动合成补丁
         * 默认为true，如果想选择合成补丁的时机，设置为false即可。
         */
        Beta.canAutoPatch = true;

        /**
         * 设置是否显示弹窗提示用户重启
         * 默认为false，如果想弹窗提示用户重启，设置为true即可。
         */
        Beta.canNotifyUserRestart = false;

        Beta.betaPatchListener = new BetaPatchListener() {

            @Override
            public void onPatchReceived(String patchFile) {
                if (Uranus.isDebug) {
                    Log.i(Uranus.TAG, "补丁下载地址" + patchFile);
                }
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                if (Uranus.isDebug) {
                    Log.i(Uranus.TAG,
                            String.format(Locale.getDefault(), "%s %d%%",
                                    Beta.strNotificationDownloading,
                                    (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)));
                }
            }

            @Override
            public void onDownloadSuccess(String msg) {
                if (Uranus.isDebug) {
                    Log.i(Uranus.TAG, "补丁下载成功" + msg);
                }
            }

            @Override
            public void onDownloadFailure(String msg) {
                if (Uranus.isDebug) {
                    Log.i(Uranus.TAG, "补丁下载失败" + msg);
                }
            }

            @Override
            public void onApplySuccess(String msg) {
                if (Uranus.isDebug) {
                    Log.i(Uranus.TAG, "补丁应用成功" + msg);
                }
            }

            @Override
            public void onApplyFailure(String msg) {
                if (Uranus.isDebug) {
                    Log.i(Uranus.TAG, "补丁应用失败" + msg);
                }
            }

            @Override
            public void onPatchRollback() {
                if (Uranus.isDebug) {
                    Log.i(Uranus.TAG, "补丁回滚");
                    if (Uranus.isDebug) {
                        Log.i(Uranus.TAG, "补丁回滚成功,设置等待重启时机标志位");
                    }
                    HotFix.setIsPatchReady(true);
                }
            }
        };
    }

    @Override
    public void down() {
        Beta.downloadPatch();
    }

    @Override
    public void apply() {
        Beta.applyDownloadedPatch();
    }

    @Override
    public void query() {
        if (Uranus.isDebug) {
            Log.i(Uranus.TAG, "tinker query");
        }
        Beta.checkUpgrade(false, true);
    }

    @Override
    public void clean() {
        Beta.cleanTinkerPatch(true);
    }

    /**
     * @see https://bugly.qq.com/docs/user-guide/faq-android-hotfix/?v=20170512172046
     * bugly 开发设备 热修复
     */
    @Override
    public void setIsDevelopmentDevice(Context context, boolean isDevelopmentDevice) {
        Bugly.setIsDevelopmentDevice(context.getApplicationContext(), isDevelopmentDevice);
    }
}
