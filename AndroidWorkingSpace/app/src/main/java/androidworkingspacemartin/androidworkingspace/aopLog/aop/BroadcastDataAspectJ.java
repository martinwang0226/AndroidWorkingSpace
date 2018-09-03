package androidworkingspacemartin.androidworkingspace.aopLog.aop;

import android.content.Intent;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Set;

import androidworkingspacemartin.androidworkingspace.aopLog.manager.LogMsgManager;
import androidworkingspacemartin.androidworkingspace.aopLog.model.EventType;
import androidworkingspacemartin.androidworkingspace.aopLog.model.MsgModel;

/**
 * Created by martinwang on 2018/1/23.
 */

@Aspect
public class BroadcastDataAspectJ {

    private String tempData = "";

    @Before("onReceive()")
    public void onBroadcastReceive(JoinPoint point) throws Throwable{
        Object[] args = point.getArgs();
        if (args.length == 2) {
            Intent data = (Intent)args[1];

            MsgModel msgModel = new MsgModel();
            msgModel.setType(EventType.TYPE_BROADCAST);
            StringBuilder sb = new StringBuilder();
            sb.append("{\"type\":\"broadcast\", \"data\":{");
            sb.append(parseString(data));
            sb.append("}}");
            msgModel.setMsg(sb.toString());
            if(!tempData.equals(sb.toString())){
                tempData = sb.toString();
                LogMsgManager.getInstance().addLogMsg(msgModel);
            }
        }
    }

    private String parseString(Intent intent){
        String str = "\"action\":\"" + intent.getAction() + "\",\"extras\":{";
        String data = "";
        try{
            if(intent != null && intent.getExtras() != null){
                Set<String> set = intent.getExtras().keySet();
                for (String key : set) {
                    data = data + "\"" + key + "\":\"" + intent.getExtras().get(key) + "\",";
                }
            }
        }catch (Exception e){

        }
        str = str + data + "}";
        return str;
    }


    @Pointcut("execution(* dazhongchuxing.dzcx_android_auto.receiver.OperationReceiver.onReceive(..))")
    public void onReceive() throws Throwable{
    }
}