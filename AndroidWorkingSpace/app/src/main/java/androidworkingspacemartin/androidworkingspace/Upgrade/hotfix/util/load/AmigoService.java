package androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.util.load;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;

/**
 * Created by martinwang on 2017/6/15.
 */

public class AmigoService extends Service {

    public static final String ACTION_RESTART_MANI_PROCESS = "restart_main_process";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void restartMainProcess(Context context) {
//        context.startService(new Intent(context, AmigoService.class)
//                .setAction(ACTION_RESTART_MANI_PROCESS));
        Process.killProcess(Process.myPid());
    }
}
