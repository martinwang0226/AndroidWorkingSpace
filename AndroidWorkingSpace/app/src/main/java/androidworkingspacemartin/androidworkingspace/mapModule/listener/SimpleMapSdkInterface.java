package androidworkingspacemartin.androidworkingspace.mapModule.listener;

import android.os.Bundle;
import android.view.View;

/**
 * Created by martinwang on 2018/3/5.
 */

public interface SimpleMapSdkInterface {
    void setOnDzcxMapListener(OnDzcxMapListener mapListener);

    View getMapView();

    void onMapCreate(Bundle savedInstanceState);

    void onMapResume();

    void onMapPause();

    void onMapSaveInstanceState(Bundle outState);

    void onMapDestory();

    /**
     * 放大缩小功能
     * @param flag
     */
    void onMapZoom(boolean flag);

    /**
     * 定位功能
     * @param lat
     * @param lot
     */
    void onMapLocaltion(Double lat, Double lot);

    /**
     * 设置规划路线的起始点与结束点
     * @param startGps
     * @param endGps
     * @param isjd
     */
    void setNaviRoute(double localLat, double localLon, String startGps, String endGps, int isjd);

    void onLocationReceive(double localLat, double localLon, int isjd);

    /**
     * 设置白天还是夜间模式
     * @param isNightMode
     */
    void setMapDayOrNightMode(boolean isNightMode);

    /**
     * 动态设置白天，黑夜模式
     * @param isNight
     */
    void onNavigationNight(Boolean isNight);

    /**
     * 刷新规划路线
     * @param typeMode
     */
    void setTypeMode(int typeMode);

    void initSetUpYueZMap();

    void setFollowMove(boolean isFollowMove);

    boolean isFollowMove();

    void setMapShowType(int mapShowType);

    void pauseNavi();

    void resumeNavi();

    void stopNavi();

    void clearRouteLine();
}
