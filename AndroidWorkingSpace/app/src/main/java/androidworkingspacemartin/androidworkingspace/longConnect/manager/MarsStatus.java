package androidworkingspacemartin.androidworkingspace.longConnect.manager;

/**
 * Created by martinwang on 2017/8/2.
 */

public class MarsStatus {
    /**
     * 延时时间
     */
    private static int[] DELAY_TIME = new int[] {2,8,16,32,64,128,128,128,256,256,256,512,512};

    private int delayIndex;

    public int getDelayTime(){
        int delayTime = delayIndex < DELAY_TIME.length ? DELAY_TIME[delayIndex] : 600;
        ++delayIndex;
        return delayTime;
    }

    public void reset(){
        delayIndex = 0;
    }
}
