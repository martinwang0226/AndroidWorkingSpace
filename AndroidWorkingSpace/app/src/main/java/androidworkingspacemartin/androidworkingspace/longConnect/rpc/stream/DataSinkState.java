package androidworkingspacemartin.androidworkingspace.longConnect.rpc.stream;

/**
 * Created by martinwang on 2017/7/27.
 */

public interface DataSinkState {

    void onDataSinkReady();

    void onDataSinkClose(Throwable t);
}

