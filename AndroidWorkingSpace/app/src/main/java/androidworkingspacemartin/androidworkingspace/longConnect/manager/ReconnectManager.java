package androidworkingspacemartin.androidworkingspace.longConnect.manager;

import android.os.Handler;
import android.os.Message;

import androidworkingspacemartin.androidworkingspace.longConnect.Mars;


/**
 * Created by martinwang on 2017/8/2.
 */

public class ReconnectManager {

    private static ReconnectManager mReconnectManager;
    private Mars mMars;
    private MarsStatus mMarsStatus;

    private ReconnectManager() {
        mMarsStatus = new MarsStatus();
    }

    public static ReconnectManager getInstance() {
        synchronized (ReconnectManager.class) {
            if (mReconnectManager == null) {
                mReconnectManager = new ReconnectManager();
            }
        }
        return mReconnectManager;
    }

    public synchronized void addMars(Mars mars) {
        //第一次加入重连的队列
        if (null == mMars) {
            mMars = mars;
            mMarsStatus.reset();
        }
        ProfileManager.getInstance().addDisconnectCount();
        handler.removeCallbacksAndMessages(null);
        //判断当前的
        int     delayTime = mMarsStatus.getDelayTime();
        Message message   = handler.obtainMessage(0);
        handler.sendMessageDelayed(message, delayTime * 100);
    }

    void reset() {
        mMarsStatus.reset();
        handler.removeCallbacksAndMessages(null);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProfileManager.getInstance().addReconnectCount();
            MarsManager.getInstance().connect(mMars);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    reset();
                }
            },10000);
        }
    };
}
