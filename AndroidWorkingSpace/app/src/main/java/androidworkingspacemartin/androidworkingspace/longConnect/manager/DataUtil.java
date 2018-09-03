package androidworkingspacemartin.androidworkingspace.longConnect.manager;

import com.google.protobuf.Timestamp;
import com.letzgo.carmonitor.CarMonitorProto;

/**
 * Created by martinwang on 2017/7/27.
 */

public class DataUtil {
    /**
     * @param la        维度
     * @param lo        经度
     * @return(lon)
     */
    public static CarMonitorProto.Status newStatus(double la, double lo, int state, String carNumber) {
        return newBuilder(carNumber)
                .setType(CarMonitorProto.Status.Type.LOCATION)
                .setLocation(CarMonitorProto.LocationStatus.newBuilder()
                        .setLatitude(la)
                        .setLongitude(lo)
                        .setLoadValue(state)
                        .build())
                .build();
    }

    public static CarMonitorProto.Status newSyncSystemTimeStatus(String carNum) {
        return newBuilder(carNum)
                .setType(CarMonitorProto.Status.Type.SYNC)
                .setSync(CarMonitorProto.Sync.newBuilder()
                        .setCarNo(carNum)
                        .build())
                .build();
    }

    private static CarMonitorProto.Status.Builder newBuilder(String carNum) {
        return CarMonitorProto.Status.newBuilder()
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(SystemTimeUtils.getInstance().getTime())
                        .build())
                .setCarNo(carNum);
    }

    /**
     * 获取一个关于统计数据的status
     * @param carNum                车牌号
     * @param connectTimes          链接的次数
     * @param disCounnectTimes      重新连接的次数
     * @param sendCount             发送消息的总数
     * @param successCount          发送成功的数量
     * @return
     */
    public static CarMonitorProto.Status newReportStatus(String carNum, int connectTimes, int disCounnectTimes, int sendCount, int successCount) {
        CarMonitorProto.Status.Builder builder = newBuilder(carNum);
        builder.setType(CarMonitorProto.Status.Type.REPORT)
                .setReport(CarMonitorProto.Report.newBuilder()
                        .setConnectTimes(connectTimes)
                        .setDisconnectTimes(disCounnectTimes)
                        .setSuccessfulMessages(successCount)
                        .setTotalMessages(sendCount)
                        .build());
        return builder.build();
    }
}
