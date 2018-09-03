package androidworkingspacemartin.androidworkingspace.aopLog.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import androidworkingspacemartin.androidworkingspace.aopLog.manager.LogMsgManager;
import androidworkingspacemartin.androidworkingspace.aopLog.model.EventType;
import androidworkingspacemartin.androidworkingspace.aopLog.model.MsgModel;

/**
 * Created by martinwang on 2018/9/3.
 */

@Aspect
public class ErrorDataAspectJ {

    @Around("execution(* com.saturn.core.component.net.RxSubscriberHelper.onShowMessage(..))")
    public Object errorData(ProceedingJoinPoint point) throws Throwable{
        Object[] args = point.getArgs();
        if (args != null && args.length > 0) {
            MsgModel errorMsg = createErrorMsg(point.getArgs()[0].toString());
            LogMsgManager.getInstance().addLogMsg(errorMsg);
        }
        Object proceed = point.proceed();
        return proceed;
    }

    private MsgModel createErrorMsg(String errorMsg){
        MsgModel msgModel = new MsgModel();
        msgModel.setType(EventType.TYPE_NET_ERROR);
        StringBuilder builder = new StringBuilder();
        builder.append("{\"type\":\"error\" ,\"message\": \"");
        builder.append(errorMsg);
        builder.append("\"");
        String s = builder.toString();
        msgModel.setMsg(s);
        return msgModel;
    }
}