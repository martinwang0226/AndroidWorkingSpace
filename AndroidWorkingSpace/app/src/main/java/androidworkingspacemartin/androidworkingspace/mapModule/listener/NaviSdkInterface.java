package androidworkingspacemartin.androidworkingspace.mapModule.listener;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by martinwang on 2018/3/5.
 */

public interface NaviSdkInterface {
    void setNaviListener(OnDzcxNaviListener naviListener);

    View getNaviView();

    void onNaviCreate(Bundle savedInstanceState);

    void onNaviResume();

    void onNaviPause();

    void onNaviSaveInstanceState(Bundle outState);

    void onNaviDestory();

    /**
     * 设置导航起始点与结束点
     * @param localLat
     * @param localLon
     * @param gpsEnd
     */
    void setNaviStartAndeEnd(Context context, Double localLat, Double localLon, String gpsEnd);
}
