package androidworkingspacemartin.androidworkingspace.Upgrade.manager;

import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;

import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.HotFix;
import androidworkingspacemartin.androidworkingspace.Upgrade.upgrade.Upgrade;

/**
 * Created by martinwang on 2017/1/20.
 */

public class Uranus {

    public final static String TAG = "Uranus";

    public static boolean isDebug ;

    public static Context sContext;

    /**
     * 手动触发升级
     */
    public static void checkUpgrade(){
        Upgrade.checkUpgrade();
    }

    /**
     * Uranus 初始化
     * @param context
     * @param uranusBuilder
     */
    public static void init(Context context, UranusBuilder uranusBuilder) {

        sContext = context.getApplicationContext();

        Uranus.isDebug = uranusBuilder.isDebug;

        /**
         * 升级更新模块
         */
        Upgrade.init(uranusBuilder);

        /**
         * 补丁模块
         */
        HotFix.init(context, uranusBuilder);

        /**
         * bugly crash update fix 模块初始化
         * https://bugly.qq.com/docs/user-guide/instruction-manual-android-upgrade/?v=20170307182353
         * 参数1：isManual 用户手动点击检查，非用户点击操作请传false
         * 参数2：isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
         */
//        Beta.checkUpgrade(false,false);

        BuglyStrategy strategy = new BuglyStrategy();
        strategy.setAppChannel(uranusBuilder.channel);

        /**
         * 升级sdk {@link https://bugly.qq.com/betaAndroidSdk}
         *
         * crash bugly文档 {@link https://bugly.qq.com/androidfast}
         *
         * true表示app启动自动初始化升级模块;
         * false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Bugly.init(context.getApplicationContext(), uranusBuilder.buglyId, uranusBuilder.isDebug, strategy);

    }
}
