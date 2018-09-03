package androidworkingspacemartin.androidworkingspace.longConnect;

/**
 * socket client 基类
 * Created by martinwang on 2017/7/27.
 *
 *
 * @param <T> client
 * @param <V> 有效弹出数据类型
 */

public abstract class SocketIOClient<T, V> implements SocketIO {

    public abstract T getClient();

    public abstract void registerConnectObserver(ConnectObserver connectObserver);

    public abstract void registerDataAvailableObserver(DataAvailableObserver dataAvailableObserver);

    public abstract void write(V v, SocketWriteObserver socketWriteObserver);
}