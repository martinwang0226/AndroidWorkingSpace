package androidworkingspacemartin.androidworkingspace.Upgrade.hotfix;

import android.content.Context;

/**
 * Created by martinwang on 2017/6/12.
 */

public interface IFix {

    /**
     * 初始化
     */
    void init();

    /**
     * 下载
     */
    void down();

    /**
     * 合成
     */
    void apply();

    /**
     * 查询新补丁
     */
    void query();

    /**
     * 清除补丁
     */
    void clean();

    /**
     * 灰度设备
     * @param context
     * @param isDevelopmentDevice
     */
    void setIsDevelopmentDevice(Context context, boolean isDevelopmentDevice);
}

