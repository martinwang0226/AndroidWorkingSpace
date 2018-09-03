package androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.util.load;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by martinwang on 2017/6/15.
 */

public class KillSelfActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
//        AmigoService.restartMainProcess(getApplicationContext());
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, KillSelfActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK |
                FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}