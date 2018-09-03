package androidworkingspacemartin.androidworkingspace.mapModule.listener;

/**
 * Created by martinwang on 2018/3/2.
 */

public interface OnDzcxNaviListener {

    /**
     * 导航初始化完成
     **/
    void onInitNaviSuccess();

    /**
     * 导航初始化完成
     **/
    void onInitNaviFailure();

    /**
     * 开始导航
     **/
    void onStartNavi(int i);

    /**
     * 取消导航
     */
    void onNaviCancel();

    /**
     * 导航透传文字回调 ---- 语音播报
     *
     * @param i
     * @param s
     */
    void onGetNavigationText(int i, String s);

    /**
     * 导航结束
     */
    void onEndEmulatorNavi();

    /**
     * 到达目的地
     */
    void onArriveDestination();
}
