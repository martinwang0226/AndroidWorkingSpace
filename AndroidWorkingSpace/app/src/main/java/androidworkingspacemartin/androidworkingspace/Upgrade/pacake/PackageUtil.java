package androidworkingspacemartin.androidworkingspace.Upgrade.pacake;

import android.content.Context;
import android.text.TextUtils;

import com.mcxiaoke.packer.helper.PackerNg;

/**
 * Created by martinwang on 2017/5/31.
 */

public class PackageUtil {

    static String sChannel;
    /**
     * 获取渠道
     * {@link https://github.com/mcxiaoke/packer-ng-plugin}
     * @return
     */
    public static String getChannel(Context context) {
        if(!TextUtils.isEmpty(sChannel)) {
            return sChannel;
        }
        // 如果没有使用PackerNg打包添加渠道，默认返回的是""
        // com.mcxiaoke.packer.helper.PackerNg
        String channel = PackerNg.getMarket(context.getApplicationContext());
        if(TextUtils.isEmpty(channel)) {
            return "default";
        }
        sChannel = channel;
        return  sChannel;
    }
}
