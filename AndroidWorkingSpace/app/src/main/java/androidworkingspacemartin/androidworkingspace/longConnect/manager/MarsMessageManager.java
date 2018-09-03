package androidworkingspacemartin.androidworkingspace.longConnect.manager;

import android.util.SparseArray;

import com.google.protobuf.Duration;
import com.letzgo.carmonitor.CarMonitorProto;

import java.util.ArrayList;

import androidworkingspacemartin.androidworkingspace.longConnect.ProfileModel;

/**
 * Mars消息管理器
 * Created by martinwang on 2017/8/2.
 */

public class MarsMessageManager {
    private static MarsMessageManager mMarsMessageManager;
    private ArrayList<Integer> mKeysList;
    private SparseArray<CarMonitorProto.Status> mMsgCache;
    private ArrayList<CarMonitorProto.Status> mPreSendMsgList;
    private MarsMessageManager(){
        mMsgCache = new SparseArray<>();
        mKeysList = new ArrayList<>();
        mPreSendMsgList = new ArrayList<>();
    }
    public static MarsMessageManager getInstance(){
        synchronized (MarsMessageManager.class){
            if (null == mMarsMessageManager){
                mMarsMessageManager = new MarsMessageManager();
            }
        }
        return mMarsMessageManager;
    }

    public void addMsgToPreSend(CarMonitorProto.Status status){
        mPreSendMsgList.add(status);
    }

    public void addMsg(CarMonitorProto.Status status){
        checkEnable();
        long id = status.getTimestamp().getSeconds();
        ProfileManager.getInstance().addSendCount();
        int putId = getId(id);
        mMsgCache.put(putId,status);
        mKeysList.add(putId);
    }

    private int getId(long id){
        return (id+"").hashCode();
    }

    private void checkEnable(){
        //每小时发送消息的数量是720
        if (mKeysList.size() >= 720) {
            //当消息数量大于720的时候 就移除10条最早的数据
            while(mKeysList.size() > 710) {
                Integer remove = mKeysList.remove(0);
                mMsgCache.remove(remove);
            }
        }
    }
    public void checkMsg(CarMonitorProto.Command command){
        if (command.getName() == CarMonitorProto.Command.Name.STATUS_REPLY) {
            long id=command.getStatusReply().getReply().getSeconds();
            //在某种情况下,当消息发送成功,可能缓存的消息已经被移除,当前的统计信息也应该确认该消息是成功的
            ProfileManager.getInstance().addSuccessCount();
            int putId = getId(id);
            mMsgCache.delete(putId);
            mKeysList.remove(Integer.valueOf(putId));
        } else if(command.getName()== CarMonitorProto.Command.Name.SYNC_REPLY) {
            SystemTimeUtils.getInstance().resetSystemTime(command.getSyncReply().getReply().getSeconds());
            for (CarMonitorProto.Status item:mPreSendMsgList){
                CarMonitorProto.Status.Builder builder = item.newBuilderForType();
                builder.setTimestamp(item.getTimestamp().newBuilderForType().setSeconds(SystemTimeUtils.getInstance().getTime(item.getTimestamp().getSeconds())).build());
                CarMonitorProto.Status newItem = builder.build();
                addMsg(newItem);
                MarsManager.getInstance().write(item);
            }
            mPreSendMsgList.clear();
        } else {
            if (command.getName() == CarMonitorProto.Command.Name.REPORT) {
                //同步统计数据
                String carNum = ProfileManager.getInstance().getCarNum();
                ProfileModel profileModel = ProfileManager.getInstance().getAnalysisInfo();
                CarMonitorProto.Status report = DataUtil.newReportStatus(carNum,profileModel.getReconnectCount(),profileModel.getDisconnectCount(),profileModel.getSuccessCount(),profileModel.getSendCount());
                addMsg(report);
                MarsManager.getInstance().write(report);
            }
        }
    }

    public void resend(MarsManager marsManager) {
        if (mMsgCache.size() > 0) {
            for (int i = 0; i < mMsgCache.size(); i++) {
                int key = mMsgCache.keyAt(i);
                //添加延迟
                CarMonitorProto.Status status = mMsgCache.get(key);
                CarMonitorProto.Status.Builder builder = status.newBuilderForType();
                CarMonitorProto.Status build = builder.setDelay(Duration.newBuilder().setSeconds(SystemTimeUtils.getInstance().getDelayTime(status)).build()).build();
                //把消息放入缓存
                mMsgCache.put(key,build);
                marsManager.write(build);
            }
        }
    }
}

