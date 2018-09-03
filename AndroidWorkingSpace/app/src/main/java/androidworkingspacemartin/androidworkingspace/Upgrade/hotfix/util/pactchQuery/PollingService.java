package androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.util.pactchQuery;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.HotFix;
import androidworkingspacemartin.androidworkingspace.Upgrade.manager.Uranus;

/**
 * Created by martinwang on 2017/6/5.
 */

public class PollingService extends Service {

    public static final String ACTION = "com.uranus.core.hotfix.service.PollingService";

    /**
     * 60s 轮询一次
     */
    public static final int IntervalMilli = 60000;

    boolean isStart = false;
    PollingThread pollingThread;

    @Override
    public IBinder onBind(Intent intent) {
        if(Uranus.isDebug) {
            Log.i(Uranus.TAG, "PollingService onBind");
        }
        return null;
    }

    @Override
    public void onCreate() {
        if(Uranus.isDebug) {
            Log.i(Uranus.TAG, "PollingService onCreate");
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if(Uranus.isDebug) {
            Log.i(Uranus.TAG, "PollingService onStart");
        }
        if(isStart == false) {
            isStart = true;
            if(pollingThread == null) {
                pollingThread = new PollingThread();
                pollingThread.start();
                if(Uranus.isDebug) {
                    Log.i(Uranus.TAG, "PollingService new start");
                }
            }
        }
    }

    class PollingThread extends Thread {
        @Override
        public void run() {
            while (isStart) {
                HotFix.queryNewPatch();
                if(Uranus.isDebug) {
                    Log.i(Uranus.TAG, "PollingService run");
                }
                try {
                    Thread.sleep(IntervalMilli);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if(Uranus.isDebug) {
            Log.i(Uranus.TAG, "PollingService onDestroy");
        }
        isStart = false;
        super.onDestroy();
    }
}