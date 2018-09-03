package androidworkingspacemartin.androidworkingspace.Upgrade.upgrade;

import android.content.Context;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;

import androidworkingspacemartin.androidworkingspace.R;
import androidworkingspacemartin.androidworkingspace.Upgrade.manager.UranusBuilder;

import static android.view.View.VISIBLE;

/**
 * Created by martinwang on 2017/8/3.
 */

public class Upgrade {

    public static void init(UranusBuilder uranusBuilder) {
        UpdateUtil.addCanNotShowUpgradeActs(uranusBuilder.canNotShowUpgradeActs);
        initBuglyUpdateConfig(uranusBuilder.updataCallBack);
    }
    public static void checkUpgrade(){
        Beta.checkUpgrade();
    }
    /**
     * bugly 更新配置初始化
     * {@link https://bugly.qq.com/docs/user-guide/advance-features-android-beta/}
     */
    private static void initBuglyUpdateConfig(final UpdateCallBack callBack) {

        /**
         * 升级检查周期设置
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        // Beta.upgradeCheckPeriod = 60 * 1000;

        /**
         * 延迟初始化
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 1 * 1000;

        /**
         * 设置通知栏大图标
         */
        // Beta.largeIconId = R.drawable.ic_launcher;

        /**
         * 设置通知栏大图标
         */
        // Beta.smallIconId = R.drawable.ic_launcher;

        /**
         * 设置更新弹窗默认展示的banner
         * defaultBannerId为项目中的图片资源Id; 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading...“;
         */
        // Beta.defaultBannerId = R.drawable.ic_launcher;

        /**
         * 设置sd卡的Download为更新资源存储目录
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /**
         * 设置开启显示打断策略
         * 设置点击过确认的弹窗在App下次启动自动检查更新时会再次显示。
         */
        Beta.showInterruptedStrategy = true;


//        /**
//         * 添加可显示弹窗的Activity
//         * 例如，只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 如果不设置默认所有activity都可以显示弹窗
//         */
//        Beta.canNotShowUpgradeActs.add(SplashActivity.class);
//        Beta.canNotShowUpgradeActs.add(AdActivity.class);

        /**
         * 设置自定义升级对话框UI布局
         * upgrade_dialog为项目的布局资源。 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用： - 特性图片：beta_upgrade_banner，如：android:tag="beta_upgrade_banner"

         标题：beta_title，如：android:tag="beta_title"

         升级信息：beta_upgrade_info 如： android:tag="beta_upgrade_info"

         更新属性：beta_upgrade_feature 如： android:tag="beta_upgrade_feature"

         取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"

         确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
         */
        Beta.upgradeDialogLayoutId = R.layout.dialog_upgrade;


        /**
         * 设置自定义tip弹窗UI布局
         * 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：

         标题：beta_title，如：android:tag="beta_title"

         提示信息：beta_tip_message 如： android:tag="beta_tip_message"

         取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"

         确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
         */
        // Beta.tipsDialogLayoutId = R.layout.favorable_exchange;


        /**
         * 设置是否显示消息通知
         * 如果你不想在通知栏显示下载进度，你可以将这个接口设置为false，默认值为true。
         */
        Beta.enableNotification = true;


        /**
         * 设置Wifi下自动下载
         * 如果你想在Wifi网络下自动下载，可以将这个接口设置为true，默认值为false。
         */
        Beta.autoDownloadOnWifi = true;


        /**
         * 设置是否显示弹窗中的apk信息
         * 如果你使用我们默认弹窗是会显示apk信息的，如果你不想显示可以将这个接口设置为false。
         */
        Beta.canShowApkInfo = false;


        /**
         *  如果想监听升级对话框的生命周期事件，可以通过设置OnUILifecycleListener接口
         *  回调参数解释：
         *  context - 当前弹窗上下文对象
         *  view - 升级对话框的根布局视图，可通过这个对象查找指定view控件
         *  upgradeInfo - 升级信息
         */
        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {

            /**
             *
             * @param context
             * @param view
             * @param upgradeInfo {@code
            public String id = "";//唯一标识
            public String title = "";//升级提示标题
            public String newFeature = "";//升级特性描述
            public long publishTime = 0;//升级发布时间,ms
            public int publishType = 0;//升级类型 0测试 1正式
            public int upgradeType = 1;//升级策略 1建议 2强制 3手工
            public int popTimes = 0;//提醒次数
            public long popInterval = 0;//提醒间隔
            public int versionCode;
            public String versionName = "";
            public String apkMd5;//包md5值
            public String apkUrl;//APK的CDN外网下载地址
            public long fileSize;//APK文件的大小
            pubilc String imageUrl; // 图片url}
             *
             */
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
                if (null != callBack) {
                    if (upgradeInfo != null) {
                        if (upgradeInfo.upgradeType == 1) {
                            callBack.suggestUpdateCreate();
                        } else {
                            callBack.constraintUpdateCreate();
                        }
                    }
                }
                final TextView textView = (TextView) view.findViewWithTag("beta_cancel_button");
                final TextView tvConfirm = (TextView) view.findViewWithTag("beta_confirm_button");
                ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
                if (upgradeInfo != null) {
                    switch (upgradeInfo.upgradeType) {
                        /**
                         * 建议升级
                         */
                        case 1:
                            ivClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (null != callBack) {
                                        callBack.suggestUpdateClose();
                                    }
                                    textView.performClick();

                                }
                            });
                            ivClose.setVisibility(VISIBLE);
                            //不要问我为什么不能写setOnClickListen 进不去
                            tvConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            tvConfirm.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    if (null != callBack) {
                                        callBack.suggestUpdate();
                                    }
                                    return false;
                                }
                            });
                            break;
                        /**
                         * 强制升级
                         */
                        case 2:
                            tvConfirm.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    if (null != callBack) {
                                        callBack.constraintUpdate();
                                    }
                                    return false;
                                }
                            });
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {
            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {
            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {

            }
        };
    }
}