package androidworkingspacemartin.androidworkingspace.longConnect;

import androidworkingspacemartin.androidworkingspace.longConnect.manager.MarsManager;

/**
 * Created by martinwang on 2017/7/27.
 *
 *  @param <T> client
 *            grpc -> {@link com.mars.core.rpc.GrpcClient}
 * @param <V> 有效弹出数据类型
 */

public class Mars<T extends SocketIOClient, V> {

    private String host;
    private int port;
    private int contType;
    public T socketIOClient;

    private Mars(Builder<T, V> builder) {
        host = builder.host;
        port = builder.port;
        contType = builder.contType;
        socketIOClient = builder.socketIOClient;
    }

    public void create() {
        if (contType == MarsManager.COUNT_HOST) {
            socketIOClient.connect(host);
        } else {
            socketIOClient.connect(host, port);
        }
    }

    public void destory() {
        socketIOClient.disConnect();
    }

    /**
     * 注册长链对象
     * @param connectObserver
     */
    public void registerConnectObserver(ConnectObserver connectObserver) {
        socketIOClient.registerConnectObserver(connectObserver);
    }

    /**
     * 注册回调数据槽
     * @param dataAvailableObserver
     */
    public void registerDataAvailableObserver(DataAvailableObserver dataAvailableObserver) {
        socketIOClient.registerDataAvailableObserver(dataAvailableObserver);
    }

    /**
     * 写入数据流
     * @param v
     * @param socketWriteObserver
     */
    public void write(V v, SocketWriteObserver socketWriteObserver) {
        socketIOClient.write(v, socketWriteObserver);
    }

    public static final class Builder<T extends SocketIOClient, V> {

        private String host;
        private int port;
        private int contType;
        private T socketIOClient;

        public Builder() {
        }

        public Builder from(String val, int val2, int val3) {
            host = val;
            port = val2;
            contType = val3;
            return this;
        }

        public Builder socketIOClient(T val) {
            socketIOClient = val;
            return this;
        }

        public Mars<T, V> build() {
            return new Mars(this);
        }
    }
}
