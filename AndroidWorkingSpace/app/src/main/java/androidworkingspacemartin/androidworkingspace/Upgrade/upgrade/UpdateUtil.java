package androidworkingspacemartin.androidworkingspace.Upgrade.upgrade;

import android.app.Activity;

import com.tencent.bugly.beta.Beta;

import java.util.List;

/**
 * Created by martinwang on 2017/8/25.
 */

public class UpdateUtil {

    /**
     * 添加可显示弹窗的Activity
     * 例如，只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 如果不设置默认所有activity都可以显示弹窗
     */
    public static void addCanNotShowUpgradeActs(List<Class<? extends Activity>> _cls) {
        if(_cls == null || _cls.isEmpty()) {
            return;
        }
        Beta.canNotShowUpgradeActs.addAll(_cls);
    }
}

