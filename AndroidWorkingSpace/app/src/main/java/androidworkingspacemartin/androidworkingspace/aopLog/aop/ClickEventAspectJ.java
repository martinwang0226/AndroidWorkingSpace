package androidworkingspacemartin.androidworkingspace.aopLog.aop;

import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import androidworkingspacemartin.androidworkingspace.aopLog.manager.LogMsgManager;
import androidworkingspacemartin.androidworkingspace.aopLog.model.EventType;
import androidworkingspacemartin.androidworkingspace.aopLog.model.MsgModel;

/**
 * Created by martinwang on 2018/9/3.
 */

@Aspect
public class ClickEventAspectJ {
    @Before("execution(* android.view.View.OnClickListener.onClick(..))")
    public void onViewClick(JoinPoint point) throws Throwable{
        Object[] args = point.getArgs();
        if (args!=null&&args.length>0&&args[0] instanceof View){
            View view= (View) point.getArgs()[0];
            MsgModel model=new MsgModel();
            model.setType(EventType.TYPE_CLICK_INT);
            StringBuilder builder=new StringBuilder();
            builder.append("{\"type\":\"viewClick\",\"page\":\"");
            builder.append(point.getThis());
            builder.append("\",\"view\":\"");
            builder.append(view);
            builder.append("\"}");
            String s = builder.toString();
            model.setMsg(s);
            LogMsgManager.getInstance().addLogMsg(model);
        }
    }
}
