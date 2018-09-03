package androidworkingspacemartin.androidworkingspace.longConnect.rpc.connect;

import java.util.ArrayList;
import java.util.List;

import androidworkingspacemartin.androidworkingspace.longConnect.ConnectObserver;
import androidworkingspacemartin.androidworkingspace.longConnect.SocketIOClient;
import androidworkingspacemartin.androidworkingspace.longConnect.rpc.GrpcClient;
import androidworkingspacemartin.androidworkingspace.longConnect.rpc.stream.DataSinkState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Created by martinwang on 2017/7/26.
 */

public class GrpcConnection implements DataSinkState, ConnectObserver{

    private ManagedChannel mChannel;
    private GrpcClient grpcClient;
    private final List<ConnectObserver> connectObserverList;

    public GrpcConnection(GrpcClient grpcClient) {
        this.grpcClient          = grpcClient;
        this.connectObserverList = new ArrayList<>();
    }

    public void connect(String host, int port) {
        mChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        this.grpcClient.grpcDataSink.init(mChannel, this);
    }

    public void connect(String host) {
        mChannel = ManagedChannelBuilder.forTarget(host).usePlaintext(true).build();
        this.grpcClient.grpcDataSink.init(mChannel, this);
    }

    public void disConnect() {
        if (mChannel != null) mChannel.shutdown();
        this.grpcClient.grpcDataSink.close();
    }

    public void reConnect() {

    }

    /**
     * 数据槽准备完毕
     */
    @Override
    public void onDataSinkReady() {

    }

    /**
     * 数据槽关闭
     */
    @Override
    public void onDataSinkClose(Throwable t) {
        onDisconnect(t);
    }

    public void registerConnectObserver(ConnectObserver connectObserver) {
        if (connectObserver == null) {
            return;
        }
        connectObserverList.add(connectObserver);
    }

    @Override
    public void onConnectCompleted(Exception ex, SocketIOClient client) {
        for (ConnectObserver connectObserver : connectObserverList) {
            connectObserver.onConnectCompleted(ex, client);
        }
    }

    @Override
    public void onDisconnect(Throwable e) {
        for (ConnectObserver connectObserver : connectObserverList) {
            connectObserver.onDisconnect(e);
        }
    }

    @Override
    public void onReconnect() {
        for (ConnectObserver connectObserver : connectObserverList) {
            connectObserver.onReconnect();
        }
    }
}
