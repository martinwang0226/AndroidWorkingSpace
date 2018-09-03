package androidworkingspacemartin.androidworkingspace.aopLog.model;

import android.content.Context;
import android.content.Intent;

/**
 * Created by martinwang on 2017/10/17.
 */

public class ProtectModel {
    private String classPath;
    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }
    public ProtectModel(String classPath){
        this.classPath=classPath;
    }
    public ProtectModel(Class clazz){
        classPath=clazz.getName();
    }
    /**
     * 重启
     * @param context
     */
    public void restart(Context context){
        Intent intent=new Intent();
        intent.setClassName(context,classPath);
        context.startService(intent);
    }

}
