package androidworkingspacemartin.androidworkingspace.aopLog.Util;

/**
 * Created by martinwang on 2017/12/11.
 */

import android.text.TextUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import androidworkingspacemartin.androidworkingspace.aopLog.helper.AppProxy;
import androidworkingspacemartin.androidworkingspace.aopLog.manager.Analysis;
import androidworkingspacemartin.androidworkingspace.aopLog.manager.LogMsgManager;
import androidworkingspacemartin.androidworkingspace.aopLog.model.EventType;
import androidworkingspacemartin.androidworkingspace.aopLog.model.MsgModel;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.BufferedSource;

public class HttpLogUtils {
    public static MsgModel createRequestMsg(Request request) {
        String url = request.url().toString();
        if (shouldStoreDeviceInfo(url)) {
            Analysis.getInstance().storeDeviceInfo();
        }
        MsgModel model = new MsgModel();
        model.setType(EventType.TYPE_REQUEST_INT);
        RequestBody body = request.body();
        okio.Buffer buffer = new okio.Buffer();
        try {
            body.writeTo(buffer);
            String formatRequest = getFormatRequest(url, buffer.readString(Charset.forName("UTF-8")));
            model.setMsg(formatRequest);
        } catch (Throwable e) {

        }
        return model;
    }

    public static boolean shouldStoreDeviceInfo(String url) {
        AppProxy appProxy = Analysis.getInstance().getAppProxy();
        String[] storeDeviceInfoJudgeUrl = appProxy.getStoreDeviceInfoJudgeUrl();
        if (storeDeviceInfoJudgeUrl == null || storeDeviceInfoJudgeUrl.length == 0) {
            return false;
        }
        for (String item : storeDeviceInfoJudgeUrl) {
            if (url.toLowerCase().contains(item.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static String getFormatRequest(String url, String param) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"type\":\"request\",\"url\":\"");
        builder.append(url);
        builder.append("\",\"gps\":");
        builder.append(Analysis.getInstance().getSatelliteCount());
        builder.append(",\"signalStrength\":");
        builder.append(Analysis.getInstance().getPhoneSignalStrength());
        builder.append(",\"token\":\"");
        builder.append(Analysis.getInstance().getAppProxy().getUserToken());
        builder.append("\",\"param\":");
        builder.append(param);
        builder.append("}");
        return builder.toString();
    }

    public static void createAndAddResponceMsg(Response response, long responceId) throws IOException {
        String result = null;
        if (HttpHeaders.hasBody(response)) {
            ResponseBody body = response.body();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            okio.Buffer buffer = source.buffer();
            result = buffer.clone().readString(Charset.forName("UTF-8"));

        }
        String url = response.request().url().toString();
        String formatResponce = getFormatResponce(url, response.code(), response.message(), result);
        MsgModel model = new MsgModel();
        model.setMsg(formatResponce);
        model.setType(EventType.TYPE_RESPONCE_INT);
        model.setId(responceId);
        LogMsgManager.getInstance().addLogMsg(model);
    }

    /**
     * @param url    地址
     * @param code   结果码
     * @param msg    信息，比如无法获取结果码，获取数据失败的消息等
     * @param result 结果 没有的话为null
     * @return
     */
    public static String getFormatResponce(String url, int code, String msg, String result) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"type\":\"responce\",\"url\":\"");
        builder.append(url);
        builder.append("\",\"code\":");
        builder.append(code);
        builder.append(",\"message\":\"");
        builder.append(msg);
        builder.append("\"");
        if (!TextUtils.isEmpty(result)) {
            builder.append(",\"result\":");
            builder.append(result);
        }
        builder.append("}");
        return builder.toString();
    }
}
