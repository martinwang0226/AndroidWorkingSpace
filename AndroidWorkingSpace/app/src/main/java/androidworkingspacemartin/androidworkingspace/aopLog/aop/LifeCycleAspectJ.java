package androidworkingspacemartin.androidworkingspace.aopLog.aop;

import android.text.TextUtils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import androidworkingspacemartin.androidworkingspace.aopLog.model.MsgModel;

/**
 * Created by martinwang on 2018/9/3.
 */

@Aspect
public class LifeCycleAspectJ {
    private String onClassName;
    private String onMethodName;

    @Before("onCreate()||onResume()||onStart()||onPause()||onRestart()||onStop()||onDestory()||onFragCreate()" +
            "||onFragResume()||onFragStart()||onFragAttach()||onFragDestory()||onFragDestoryView()||onFragPause()||" +
            "onFragStop()||onFragDetach()||onFragCreateView()")
    public void onLifeCycleChange(JoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getThis().toString();
        String methodName = joinPoint.getSignature().getName();
        if (isClassNameEnable(className) || isMethodNameEnable(methodName)) {
            onClassName = className;
            onMethodName = methodName;
            Signature signature = joinPoint.getSignature();
            MsgModel model=new MsgModel();
//            model.setType(EventType.TYPE_LIFE_CYCLE);
            StringBuilder builder=new StringBuilder();
            builder.append("{\"type\":\"lifecycle\",\"page\":\"");
            builder.append(joinPoint.getThis());
            builder.append("\",\"method\":\"");
            builder.append(signature.getName());
            builder.append("\"}");
            String s = builder.toString();
            model.setMsg(s);
//            LogMsgManager.getInstance().addLogMsg(model);
        }
    }

    private boolean isClassNameEnable(String className) {
        if (TextUtils.isEmpty(onClassName) || !TextUtils.equals(onClassName, className)) {
            return true;
        }
        return false;
    }

    private boolean isMethodNameEnable(String methodName) {
        if (TextUtils.isEmpty(onMethodName) || !TextUtils.equals(onMethodName, methodName)) {
            return true;
        }
        return false;
    }

    @Pointcut("execution(* android.app.Activity.onCreate(..))")
    public void onCreate() {
    }
    @Pointcut("execution(* android.support.v4.app.Fragment.onCreate(..))")
    public void onFragCreate() {
    }

    @Pointcut("execution(* android.app.Activity.onResume(..))")
    public void onResume() {
    }
    @Pointcut("execution(* android.support.v4.app.Fragment.onResume(..))")
    public void onFragResume() {
    }

    @Pointcut("execution(* android.app.Activity.onStart(..))")
    public void onStart() {
    }
    @Pointcut("execution(* android.support.v4.app.Fragment.onStart(..))")
    public void onFragStart() {
    }

    @Pointcut("execution(* android.app.Activity.onPause(..))")
    public void onPause() {
    }
    @Pointcut("execution(* android.support.v4.app.Fragment.onPause(..))")
    public void onFragPause() {
    }

    @Pointcut("execution(* android.app.Activity.onRestart(..))")
    public void onRestart() {
    }

    @Pointcut("execution(* android.app.Activity.onStop(..))")
    public void onStop() {
    }
    @Pointcut("execution(* android.support.v4.app.Fragment.onStop(..))")
    public void onFragStop() {
    }

    @Pointcut("execution(* android.app.Activity.onDestory(..))")
    public void onDestory() {
    }
    @Pointcut("execution(* android.support.v4.app.Fragment.onDestory(..))")
    public void onFragDestory() {
    }
    @Pointcut("execution(* android.support.v4.app.Fragment.onAttach(..))")
    public void onFragAttach() {
    }
    @Pointcut("execution(* android.support.v4.app.Fragment.onDetach(..))")
    public void onFragDetach() {
    }
    @Pointcut("execution(* android.support.v4.app.Fragment.onCreateView(..))")
    public void onFragCreateView() {
    }
    @Pointcut("execution(* android.support.v4.app.Fragment.onDestoryView(..))")
    public void onFragDestoryView() {
    }


}

