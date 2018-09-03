package androidworkingspacemartin.androidworkingspace.longConnect;

import android.text.TextUtils;

import org.json.JSONObject;

/**
 * Created by martinwang on 2017/8/8.
 */

public class ProfileModel {
    private static final String COUNT_DIS_CONNECT = "count_dis_connect";
    private static final String COUNT_RE_CONNECT  = "count_re_connect";
    private static final String COUNT_SEND        = "count_send";
    private static final String COUNT_SUCCESS     = "count_success";

    private int disconnectCount;
    private int reconnectCount;
    private int sendCount;
    private int successCount;
    private StringBuilder mBuilder;

    public int getDisconnectCount() {
        return disconnectCount;
    }

    public void setDisconnectCount(int disconnectCount) {
        this.disconnectCount = disconnectCount;
    }

    public int getReconnectCount() {
        return reconnectCount;
    }

    public void setReconnectCount(int reconnectCount) {
        this.reconnectCount = reconnectCount;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public static ProfileModel create(String profileInfo) {
        if (TextUtils.isEmpty(profileInfo)) {
            return new ProfileModel();
        } else {
            return createFromJson(profileInfo);
        }
    }

    private static final ProfileModel createFromJson(String profileInfo) {
        ProfileModel model = new ProfileModel();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(profileInfo);
        } catch (Exception e) {

        }
        if (jsonObject != null) {
            int disconnectCount = jsonObject.optInt(COUNT_DIS_CONNECT, 0);
            int reconnectCount  = jsonObject.optInt(COUNT_RE_CONNECT, 0);
            int sendCount       = jsonObject.optInt(COUNT_SEND, 0);
            int successCount    = jsonObject.optInt(COUNT_SUCCESS);
            model.setDisconnectCount(disconnectCount);
            model.setReconnectCount(reconnectCount);
            model.setSendCount(sendCount);
            model.setSuccessCount(successCount);
        }
        return model;
    }

    public void addDisconnectCount() {
        ++disconnectCount;
    }

    public void addReconnectCount() {
        ++reconnectCount;
    }

    public void addSendCount() {
        ++sendCount;
    }

    public void addSuccessCount() {
        ++successCount;
    }

    public String toJson() {
        if (mBuilder == null) {
            mBuilder = new StringBuilder();
        }
        mBuilder.delete(0, mBuilder.length());
        mBuilder.append("{\"");
        mBuilder.append(COUNT_DIS_CONNECT);
        mBuilder.append("\":");
        mBuilder.append(disconnectCount);
        mBuilder.append(",\"");
        mBuilder.append(COUNT_RE_CONNECT);
        mBuilder.append("\":");
        mBuilder.append(reconnectCount);
        mBuilder.append(",\"");
        mBuilder.append(COUNT_SEND);
        mBuilder.append("\":");
        mBuilder.append(sendCount);
        mBuilder.append(",\"");
        mBuilder.append(COUNT_SUCCESS);
        mBuilder.append("\":");
        mBuilder.append(successCount);
        mBuilder.append("}");
        return mBuilder.toString();
    }
}