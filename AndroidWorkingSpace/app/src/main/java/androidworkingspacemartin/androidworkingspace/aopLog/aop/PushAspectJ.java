package androidworkingspacemartin.androidworkingspace.aopLog.aop;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidworkingspacemartin.androidworkingspace.aopLog.Service.LogService;
import androidworkingspacemartin.androidworkingspace.aopLog.manager.LogMsgManager;
import androidworkingspacemartin.androidworkingspace.aopLog.model.EventType;
import androidworkingspacemartin.androidworkingspace.aopLog.model.MsgModel;

/**
 * Created by martinwang on 2017/9/14.
 */

@Aspect
public class PushAspectJ {
    @Before("pushReceive()")
    public void pushReceive(JoinPoint point) throws  Throwable{
        Log.i("chen","push：-aaaaaaaaaaaaaaaaaaaaa");
        Object[] args = point.getArgs();
        if (args.length==1){
            String msg= (String) args[0];
            MsgModel model=new MsgModel();
            model.setType(EventType.TYPE_PUSH_INT);
            StringBuilder builder=new StringBuilder();
            builder.append("{\"type\":\"push\",\"method\":\"");
            builder.append(point.getSignature().getName());
            builder.append("\",\"pushMsg\":\"");
            builder.append(msg);
            builder.append("\"}");
            String s = builder.toString();
            model.setMsg(s);
            LogMsgManager.getInstance().addLogMsg(model);
        }
    }

    @Before("messageReceive()")
    public void messageReceive(JoinPoint point) throws  Throwable{
        Log.i("chen","开始上传文件：-aaaaaaaaaaaaaaaaaaaaa");
        Object[] args = point.getArgs();
        if (args.length==2){
            String msg= (String) args[1];
            try{
                JSONObject jsonObject=new JSONObject(msg);
                String type = jsonObject.optString("type");
                if ("uploadCacheFile".equals(type)){
                    Log.i("chen","开始上传文件：-bbbbbbbbbbbbbbbb");
                    long date=jsonObject.optLong("pushDate");
                    boolean useDB=jsonObject.optBoolean("useDb",false);
                    String id=jsonObject.optString("id",System.nanoTime()+"");
                    String formatDate="";
                    if (date>0){
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                        formatDate = format.format(new Date(date));
                    }
                    Log.i("chen","开始上传文件：-cccccccccccccccccccc");
                    Context context= (Context) args[0];
                    Intent intent=new Intent(context,LogService.class);
                    intent.putExtra(LogService.ACTION_PARAM_USE_DB,useDB);
                    intent.putExtra(LogService.ACTION_PARAM_TIME, formatDate);
                    intent.putExtra(LogService.ACTION_PARAM_ID,id);
                    intent.setAction(LogService.ACTION_UPLOAD);
                    context.startService(intent);
                }
            }catch (Exception e){
                Log.i("chen","开始上传文件：-异常====================》");
            }

        }
    }

    @Pointcut("execution(* com.saturn.core.component.push.jpush.JPushReceiver.messageReceive(..))")
    public void messageReceive() throws Throwable{

    }
    @Pointcut("execution(* com.saturn.core.component.push.jpush.JPushReceiver.notificationOpenedReceive(..))")
    public void notificationOpenedReceive() throws Throwable{

    }

    @Pointcut("execution(* dazhongchuxing.dzcx_android_auto.service.OrderServiceHandler.handlePushData(..))")
    public void pushReceive() throws Throwable{

    }

}