package androidworkingspacemartin.androidworkingspace.mapModule.listener;

import android.view.MotionEvent;

/**
 * Created by martinwang on 2018/3/2.
 */

public interface OnDzcxMapListener {

    /**
     * 定位改变
     */
    void onMyLocationChange(double localLat, double localLon);

    /**
     * 地图触摸事件
     */
    void onTouchView(MotionEvent motionEvent);
}
