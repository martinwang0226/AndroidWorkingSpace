package androidworkingspacemartin.androidworkingspace.longConnect;

/**
 * Created by martinwang on 2017/7/26.
 */

public interface ConnectObserver<T extends SocketIOClient> {

    void onConnectCompleted(Exception ex, T client);

    void onDisconnect(Throwable e);

    void onReconnect();
}

