package androidworkingspacemartin.androidworkingspace.longConnect;

/**
 * Created by martinwang on 2017/7/27.
 */

public interface SocketIO {

    void connect(String host, int port);

    void connect(String host);

    void disConnect();
}

