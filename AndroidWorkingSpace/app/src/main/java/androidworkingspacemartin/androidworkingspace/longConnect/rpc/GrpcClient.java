package androidworkingspacemartin.androidworkingspace.longConnect.rpc;

import androidworkingspacemartin.androidworkingspace.longConnect.ConnectObserver;
import androidworkingspacemartin.androidworkingspace.longConnect.DataAvailableObserver;
import androidworkingspacemartin.androidworkingspace.longConnect.SocketWriteObserver;
import androidworkingspacemartin.androidworkingspace.longConnect.rpc.connect.GrpcConnection;
import androidworkingspacemartin.androidworkingspace.longConnect.rpc.stream.GrpcDataSink;

/**
 * Created by martinwang on 2017/7/26.
 * @param <V> client 输入数据类型
 */

public class GrpcClient<V> extends GrpcIOClient<V> {


    @Override
    public GrpcDataSink getClient() {
        return grpcDataSink;
    }

    /**
     * 连接控制
     */
    private GrpcConnection grpcGrpcConnection;
    /**
     * 数据槽
     */
    public GrpcDataSink grpcDataSink;


    public GrpcClient(GrpcDataSink grpcDataSink) {
        this.grpcDataSink       = grpcDataSink;
        this.grpcGrpcConnection = new GrpcConnection(this);
    }

    @Override
    public void connect(String host, int port) {
        grpcGrpcConnection.connect(host, port);
    }

    @Override
    public void connect(String host) {
        grpcGrpcConnection.connect(host);
    }

    public void disConnect() {
        grpcGrpcConnection.disConnect();
    }

    /**
     * 发送数据
     */
    @Override
    public void write(V v, SocketWriteObserver socketWriteObserver) {
        grpcDataSink.write(v, socketWriteObserver);
    }

    /**
     * 注册连接状态监听
     *
     * @param connectObserver
     */
    @Override
    public void registerConnectObserver(ConnectObserver connectObserver) {
        grpcGrpcConnection.registerConnectObserver(connectObserver);
    }

    /**
     * 注册数据回调
     *
     * @param dataAvailableObserver
     */
    @Override
    public void registerDataAvailableObserver(DataAvailableObserver dataAvailableObserver) {
        grpcDataSink.registerGrpcDataAvailableObserver(dataAvailableObserver);
    }
}