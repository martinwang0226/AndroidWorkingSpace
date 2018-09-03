package androidworkingspacemartin.androidworkingspace.longConnect.manager;

import android.content.SharedPreferences;
import android.os.AsyncTask;

/**
 * Created by martinwang on 2017/7/3.
 */

public class SharedPreferencesSync extends AsyncTask<String, Void, Boolean> {

    private SharedPreferences mSharedProference;

    public SharedPreferencesSync(SharedPreferences mSharedProference) {
        this.mSharedProference = mSharedProference;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        synchronized (mSharedProference) {
            SharedPreferences.Editor editor = mSharedProference.edit();
            editor.putString(params[0], params[1]);
            Boolean flag = editor.commit();
            //Log.e("mars", "===========>保存本地数据  params[0:]"+params[0]+"   params[1]:"+params[1]+"   flag:"+flag);
            return flag;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        cancel(true);
    }

}
