package androidworkingspacemartin.androidworkingspace.mapModule.listener;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by martinwang on 2018/3/5.
 */

public interface MapSdkInterface {

    void setOnDzcxMapListener(OnDzcxMapListener mapListener);

    View getMapView();

    void onMapCreate(Bundle savedInstanceState);

    void onMapResume();

    void onMapPause();

    void onMapSaveInstanceState(Bundle outState);

    void onMapDestory();

    /**
     * 自定义地图显示模式
     * @param mode
     * @param mTop
     * @param mBottom
     */
    void onMapDayOrNightMode(boolean mode, View mTop, View mBottom);

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
     * 路线规划功能
     * @param startGps
     * @param endGps
     */
    void onMapRoute(String startGps, String endGps);

    /**
     * 定位后重新规划路线
     * @param localLat
     * @param localLon
     * @param isjd
     */
    void onLocationReceive(double localLat, double localLon, int isjd);

    /**
     * 设置规划路线的起始点与结束点
     * @param startGps
     * @param endGps
     * @param isjd
     */
    void setNaviRoute(String startGps, String endGps, int isjd);

    /**
     * 设置白天还是夜间模式
     * @param isNightMode
     */
    void setMapDayOrNightMode(boolean isNightMode);

    /**
     * 默认地图显示带路线规划功能
     * @param context
     * @param startGps
     * @param endGps
     */
    void setMapViewRouteShow(Context context, String startGps, String endGps);

    /**
     * 动态设置白天，黑夜模式
     * @param isNight
     */
    void onNavigationNight(Boolean isNight);

    /**
     * 刷新规划路线
     * @param isjd
     */
    void reNaviRoute(int isjd);

    /**
     * 刷新规划路线
     * @param typeMode
     */
    void setTypeMode(int typeMode);

    void initSetUpYueZMap();

    void setSensorEvent();

    void setEndLatLonPoint(String gpsEnd, int drawableId);

    void setStartLatLonPoint(String gpsStart, int drawableId);

    void setEndLatLonPoint(Double gpsLat, Double gpsLot, int drawableId);

    void setStartLatLonPoint(Double gpsLat, Double gpsLot, int drawableId);

    void setFollowMove(boolean isFollowMove);

    boolean isFollowMove();

    void calculateDistance(Context context, String location, String startGps, OnCalculateDistanceListener listener);

    void setMapShowType(int mapShowType);
}