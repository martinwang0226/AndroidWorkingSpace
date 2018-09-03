package androidworkingspacemartin.androidworkingspace.longConnect.rpc.stream;

import java.util.ArrayList;
import java.util.List;

import androidworkingspacemartin.androidworkingspace.longConnect.DataAvailableObserver;
import androidworkingspacemartin.androidworkingspace.longConnect.SocketWriteObserver;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

/**
 * Created by martinwang on 2017/7/27.
 * @param <T> 用户端写入类型
 * @param <E> 服务端写入类型
 */

public abstract class GrpcDataSink<T, E> implements DataSinkState, DataAvailableObserver<E> {

    /**
     * 数据槽状态
     */
    protected DataSinkState dataSinkState;
    /**
     * 数据流
     */
    protected StreamObserver<T> streamObserver;
    /**
     * 数据流监听
     */
    private final List<SocketWriteObserver> grpcDataObserverList = new ArrayList<>();

    public StreamObserver<T> init(ManagedChannel managedChannel, DataSinkState dataSinkState) {
        this.streamObserver = create(managedChannel);
        this.dataSinkState  = dataSinkState;
        return streamObserver;
    }

    public void close() {
        if (streamObserver != null) {
            streamObserver.onCompleted();
        }
        streamObserver = null;
    }

    /**
     * client stream
     *
     * @return
     */
    protected abstract StreamObserver<T> create(ManagedChannel managedChannel);

    /**
     * server stream
     *
     * @return
     */
    protected StreamObserver<E> create() {
        return new StreamObserver<E>() {
            @Override
            public void onNext(E e) {
                onDataAvailable(e);
            }

            @Override
            public void onError(Throwable e) {
                onDataSinkClose(e);
            }

            @Override
            public void onCompleted() {
                onDataSinkClose(null);
            }
        };
    }

    /**
     * 数据槽准备完毕
     */
    @Override
    public void onDataSinkReady() {
        if (dataSinkState != null) {
            dataSinkState.onDataSinkClose(null);
        }
    }

    @Override
    public void onDataSinkClose(Throwable t) {
        if (dataSinkState != null) {
            dataSinkState.onDataSinkClose(t);
        }
    }

    /**
     * 有效数据弹出
     *
     * @param t
     */
    @Override
    public void onDataAvailable(E t) {
        for (DataAvailableObserver dataAvailableObserver : dataAvailableObserverList) {
            dataAvailableObserver.onDataAvailable(t);
        }
    }

    public void write(T note, SocketWriteObserver socketWriteObserver) {
        if (streamObserver == null) {
            if (socketWriteObserver != null) {
                socketWriteObserver.onError(new NullPointerException("not connect!"));
            }
            return;
        }
        grpcDataObserverList.add(socketWriteObserver);
        streamObserver.onNext(note);
    }

    private final List<DataAvailableObserver> dataAvailableObserverList = new ArrayList<>();

    public void registerGrpcDataAvailableObserver(DataAvailableObserver dataAvailableObserver) {
        if (dataAvailableObserver == null) {
            return;
        }
        dataAvailableObserverList.add(dataAvailableObserver);
    }
}
