package androidworkingspacemartin.androidworkingspace.aopLog.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.json.JSONObject;

import androidworkingspacemartin.androidworkingspace.aopLog.Util.HttpLogUtils;
import androidworkingspacemartin.androidworkingspace.aopLog.Util.HttpUtils;
import androidworkingspacemartin.androidworkingspace.aopLog.manager.Analysis;
import androidworkingspacemartin.androidworkingspace.aopLog.manager.LogMsgManager;
import androidworkingspacemartin.androidworkingspace.aopLog.model.EventType;
import androidworkingspacemartin.androidworkingspace.aopLog.model.MsgModel;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by martinwang on 2018/9/3.
 */

@Aspect
public class NetDataAspectJ {

    @Around("execution(* okhttp3.logging.HttpLoggingInterceptor.intercept(..))")
    public Object netDataOkhttp(ProceedingJoinPoint point) throws Throwable{
        Object[] args = point.getArgs();
        long requestId=0;
        if (args!=null&&args.length>0&&args[0] instanceof Interceptor.Chain){
            Interceptor.Chain chain= (Interceptor.Chain) args[0];
            Request request = chain.request();
            MsgModel model = HttpLogUtils.createRequestMsg(request);
            requestId=model.getId();
            LogMsgManager.getInstance().addLogMsg(model);
        }
        Object proceed = point.proceed();
        if (proceed!=null){
            Response response= (Response) proceed;
            HttpLogUtils.createAndAddResponceMsg(response,requestId);
        }
        return proceed;
    }

    @Around("execution(* com.visionet.dazhongcx.**.HttpRequest.sendPost(..))")
    public Object netDataWithTask(ProceedingJoinPoint point) throws Throwable{
        Object[] args = point.getArgs();
        long   requestId = 0;
        String url       = null;
        if (args.length == 3) {
            url = (String) args[0];
            if (HttpLogUtils.shouldStoreDeviceInfo(url)){
                Analysis.getInstance().storeDeviceInfo();
            }
            JSONObject paramsObject = (JSONObject) args[1];
            MsgModel   model        = new MsgModel();
            model.setType(EventType.TYPE_REQUEST_INT);
            String formatRequest = HttpLogUtils.getFormatRequest(url,paramsObject.toString());
            model.setMsg(formatRequest);
            requestId = model.getId();
            LogMsgManager.getInstance().addLogMsg(model);
        }
        Object proceed = point.proceed();
        if (proceed != null) {
            String response = (String) proceed;
            String formatResponce = HttpLogUtils.getFormatResponce(url,-1,"can not get responce code",response);
            MsgModel model = new MsgModel();
            model.setMsg(formatResponce);
            model.setType(EventType.TYPE_RESPONCE_INT);
            model.setId(requestId);
            LogMsgManager.getInstance().addLogMsg(model);
        }

        return proceed;
    }
}

