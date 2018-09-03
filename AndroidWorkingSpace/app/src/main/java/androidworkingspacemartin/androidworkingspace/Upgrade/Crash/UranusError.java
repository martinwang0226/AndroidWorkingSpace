package androidworkingspacemartin.androidworkingspace.Upgrade.Crash;

import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;

import androidworkingspacemartin.androidworkingspace.Upgrade.manager.Uranus;

/**
 * Created by martinwang on 2017/6/21.
 */

public class UranusError extends Throwable {

    public static UranusError create(Throwable cause, String... datas) {
        StringBuilder result = new StringBuilder();
        if (datas != null) {
            for (String str : datas) {
                if (!TextUtils.isEmpty(result)) {
                    result.append("\r\n");
                }
                result.append(str);
            }
        }
        if(TextUtils.isEmpty(result.toString())) {
            return new UranusError(cause);
        }
        return new UranusError(result.toString(), cause);
    }

    public static UranusError create(String key, String value, Throwable cause, String... datas) {
        StringBuilder result = new StringBuilder();
        if (datas != null) {
            for (String str : datas) {
                if (!TextUtils.isEmpty(result)) {
                    result.append("\r\n");
                }
                result.append(str);
            }
        }
        CrashReport.putUserData(Uranus.sContext, key, value);
        return new UranusError(result.toString(), cause);
    }

    private UranusError(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

    private UranusError(Throwable cause) {
        super(cause);
    }

}
