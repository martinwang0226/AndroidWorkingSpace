package androidworkingspacemartin.androidworkingspace.longConnect.manager;

import android.content.Context;

import com.letzgo.carmonitor.CarMonitorProto;

import androidworkingspacemartin.androidworkingspace.longConnect.ConnectObserver;
import androidworkingspacemartin.androidworkingspace.longConnect.DataAvailableObserver;
import androidworkingspacemartin.androidworkingspace.longConnect.Mars;
import androidworkingspacemartin.androidworkingspace.longConnect.SocketIOClient;
import androidworkingspacemartin.androidworkingspace.longConnect.SocketWriteObserver;


/**
 * 管理所有的Mars对象
 * Created by martinwang on 2017/8/2.
 */

public class MarsManager {

    public static final int COUNT_HOST_PORT = 0;
    public static final int COUNT_HOST      = 1;

    private static MarsManager mMarsManager;
    private Mars mMars;
    private ConnectObserver mConnectObserver;

    private MarsManager() {

    }

    public static MarsManager getInstance() {
        synchronized (MarsManager.class) {
            if (mMarsManager == null) {
                mMarsManager = new MarsManager();
                //初始化断开重连的管理器
                ReconnectManager.getInstance();
            }
        }
        return mMarsManager;
    }

    protected Mars get() {
        return mMars;
    }

    /**
     * 连接服务器
     * @param mars
     */
    void connect(final Mars mars) {
        mars.create();
        if (mConnectObserver == null) {
            mConnectObserver = new ConnectObserver() {
                @Override
                public void onConnectCompleted(Exception ex, SocketIOClient client) {
                    //创建连接 成功的时候执行,暂时没有执行
                }

                @Override
                public void onDisconnect(Throwable e) {
                    //因为出现异常 需要重新连接
                    if (e != null) {
                        ReconnectManager.getInstance().addMars(mars);
                    }
                }

                @Override
                public void onReconnect() {
                    //重连的时候执行的事件
                }
            };
            mMars.registerConnectObserver(mConnectObserver);
            mMars.registerDataAvailableObserver(new DataAvailableObserver<CarMonitorProto.Command>() {
                @Override
                public void onDataAvailable(CarMonitorProto.Command model) {
                    MarsMessageManager.getInstance().checkMsg(model);
                }
            });
        }
        MarsMessageManager.getInstance().resend(this);
    }

    /**
     * 创建mars对象
     * @param host
     * @param port
     * @param client
     * @return
     */
    public synchronized MarsManager create(Context context, String host, int port, int conType, SocketIOClient client){
        //初始化性能分析的管理器
        ProfileManager.init(context);
        if(null == mMars) {
            //创建长连接
            mMars = new Mars.Builder().from(host, port, conType).socketIOClient(client).build();
            //连接
            connect(mMars);
        }
        return this;
    }

    public synchronized void write(double la, double lo, String carNumber, int state){
        ProfileManager.getInstance().saveCarNum(carNumber);
        CarMonitorProto.Status status = DataUtil.newStatus(la, lo, state, carNumber);
        if (SystemTimeUtils.getInstance().isSystemTimeEnabled()) {
            MarsMessageManager.getInstance().addMsg(status);
            write(status);
        } else {
            MarsMessageManager.getInstance().addMsgToPreSend(status);
            CarMonitorProto.Status syncStatus = DataUtil.newSyncSystemTimeStatus(carNumber);
            MarsMessageManager.getInstance().addMsg(syncStatus);
            write(syncStatus);
        }
    }

    void write(CarMonitorProto.Status status){
        if (null != mMars) {
            mMars.write(status, new SocketWriteObserver<CarMonitorProto.Status>() {
                @Override
                public void onNext(CarMonitorProto.Status value) {

                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {

                }
            });
        }
    }

    /**
     * 注册数据监听回调
     * @param observer
     * @param <V>
     */
    public <V> void addDataListener(DataAvailableObserver<V> observer){
        get().registerDataAvailableObserver(observer);
    }

    /**
     * 断开连接 会置空连接的对象
     *
     */
    public void disConnect(){
        Mars mars = get();
        if (mars != null) {
            mars.destory();
            mars = null;
        }
    }
}
