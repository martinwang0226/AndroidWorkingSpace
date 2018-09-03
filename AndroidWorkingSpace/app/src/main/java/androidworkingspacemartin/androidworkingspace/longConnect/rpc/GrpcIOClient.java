package androidworkingspacemartin.androidworkingspace.longConnect.rpc;

import androidworkingspacemartin.androidworkingspace.longConnect.SocketIOClient;
import androidworkingspacemartin.androidworkingspace.longConnect.rpc.stream.GrpcDataSink;

/**
 * Created by martinwang on 2017/7/27.
 */

public abstract class GrpcIOClient<V> extends SocketIOClient<GrpcDataSink, V> {

}
