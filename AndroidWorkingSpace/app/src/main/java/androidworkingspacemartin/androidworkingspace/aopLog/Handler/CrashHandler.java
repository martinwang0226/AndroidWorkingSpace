package androidworkingspacemartin.androidworkingspace.aopLog.Handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import androidworkingspacemartin.androidworkingspace.aopLog.manager.LogMsgManager;
import androidworkingspacemartin.androidworkingspace.aopLog.model.EventType;
import androidworkingspacemartin.androidworkingspace.aopLog.model.MsgModel;

/**
 * Created by martinwang on 2017/9/15.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler handler;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private CrashHandler(){
        mDefaultHandler=Thread.getDefaultUncaughtExceptionHandler();
    }
    public static CrashHandler getInstance(){
        if (handler==null){
            handler=new CrashHandler();
        }
        return handler;
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        storeException(ex);
        if (mDefaultHandler!=null){
            mDefaultHandler.uncaughtException(thread,ex);
        }
    }
    public void init(){
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    private String exception(Throwable t) throws IOException {
        if(t == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            t.printStackTrace(new PrintStream(baos));
        }finally{
            baos.close();
        }
        return baos.toString();
    }
    private void storeException(Throwable throwable){
        try {
            String exceptionLog=exception(throwable);
            MsgModel model=new MsgModel();
            model.setType(EventType.TYPE_EXCEPTION);
            model.setMsg(getExceptionMsg(exceptionLog));
            LogMsgManager.getInstance().addLogMsg(model);
            LogMsgManager.getInstance().storeLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getExceptionMsg(String exception){
        StringBuilder builder=new StringBuilder();
        builder.append("{\"type\":\"exception\",\"msg\":\"");
        builder.append(exception);
        builder.append("\"}");
        return builder.toString();
    }
}