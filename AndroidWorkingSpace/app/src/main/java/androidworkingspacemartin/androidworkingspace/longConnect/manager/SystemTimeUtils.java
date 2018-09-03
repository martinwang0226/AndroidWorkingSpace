package androidworkingspacemartin.androidworkingspace.longConnect.manager;

import com.letzgo.carmonitor.CarMonitorProto;

/**
 * Created by martinwang on 2017/9/22.
 */

public class SystemTimeUtils {
    private static SystemTimeUtils mSystemTimeUtils = new SystemTimeUtils();
    private long mSystemTime = 0;
    private long mLocalTime;

    private SystemTimeUtils() {

    }

    public static  SystemTimeUtils getInstance(){
        return mSystemTimeUtils;
    }

    public void resetSystemTime(long systemTime) {
        this.mSystemTime = systemTime;
        this.mLocalTime  = System.currentTimeMillis()/1000;
    }

    /**
     * 获取当前时间的服务端时间
     * @return
     */
    public long getTime(){
        return getTime(System.currentTimeMillis()/1000);
    }

    /**
     * 获取相对某个时间的服务端时间
     * @param time  单位秒
     * @return
     */
    public long getTime(long time){
        if (isSystemTimeEnabled()) {
            return mSystemTime + time - mLocalTime;
        } else {
            return time;
        }
    }

    public long getDelayTime(CarMonitorProto.Status status) {
        return getTime() - status.getTimestamp().getSeconds();
    }

    /**
     * 判断当前的服务端时间是否可用
     * @return
     */
    public boolean isSystemTimeEnabled() {
        return mSystemTime != 0;
    }
}
