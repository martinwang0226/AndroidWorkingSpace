package androidworkingspacemartin.androidworkingspace.longConnect.rpc.route;

import com.letzgo.carmonitor.CarMonitorProto;
import com.letzgo.carmonitor.FeedServiceGrpc;

import androidworkingspacemartin.androidworkingspace.longConnect.rpc.stream.GrpcDataSink;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

/**
 * Created by martinwang on 2017/7/27.
 */

public class FeedGrpcDataSink extends GrpcDataSink<CarMonitorProto.Status, CarMonitorProto.Command> {

    @Override
    public StreamObserver<CarMonitorProto.Status> create(ManagedChannel managedChannel) {
        return FeedServiceGrpc.newStub(managedChannel).feed(create());
    }
}
