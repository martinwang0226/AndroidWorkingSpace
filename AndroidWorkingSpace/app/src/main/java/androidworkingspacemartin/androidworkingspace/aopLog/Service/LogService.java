package androidworkingspacemartin.androidworkingspace.aopLog.Service;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidworkingspacemartin.androidworkingspace.aopLog.helper.LogDBHelper;
import androidworkingspacemartin.androidworkingspace.R;
import androidworkingspacemartin.androidworkingspace.aopLog.Util.DeviceNetFlowInfoUtils;
import androidworkingspacemartin.androidworkingspace.aopLog.Util.HttpUtils;
import androidworkingspacemartin.androidworkingspace.aopLog.Util.LogFileUtils;
import androidworkingspacemartin.androidworkingspace.aopLog.Util.SystemUtils;
import androidworkingspacemartin.androidworkingspace.aopLog.manager.Analysis;
import androidworkingspacemartin.androidworkingspace.aopLog.manager.LogDBManager;
import androidworkingspacemartin.androidworkingspace.aopLog.manager.LogMsgManager;

/**
 * Created by martinwang on 2017/9/14.
 */

public class LogService extends Service {
    public static final long MIN_SIZE_STORE=1024*1024*40;
    //todo 这里的代码需要删除
//    public static final long MAX_SIZE_FILE=1024*300;
    public static final long MAX_SIZE_FILE=1024*1024*40;
    public static final String REGEX_PATTERN="^[0-9]{11,}_[0-9]{11,}\\.zip$";
    /**
     *
     * 上传日志
     */
    private static final int TYPE_UPLOAD=0;
    /**
     * 保存log日志到数据
     */
    private static final int TYPE_STORE_LOG=1;
    /**
     * 检查数据库，删除过期数据
     */
    private static final int TYPE_DATABASE_CHECK=2;
    /**
     * 保存设备信息
     */
    private static final int TYPE_STORE_DEVICE_INFO =3;
    /**
     * 保存日志的时间间隔
     */
    private static final int STORE_LOG_DELAY_TIME=20*1000;
    /**
     * 保存设备信息
     */
    public static final String ACTION_STORE_DEVICE_INFO="action_store_device_info";
    /**
     * 需要使用service上传log的时候使用的action
     */
    public static final String ACTION_UPLOAD="logservice.upload";
    /**
     * 检查数据库的数据
     */
    public static final String ACTION_CHECK="logservice.check";
    /**
     * String 上传日志的时候使用的time
     */
    public static final String ACTION_PARAM_TIME="param_time";
    /**
     * 上传文件的唯一id
     */
    public static final String ACTION_PARAM_ID="param_push_id";
    /**
     * boolean 上传日志的时候使用的，表示是否是上传db文件
     */
    public static final String ACTION_PARAM_USE_DB="use_db";

    private Handler handler;
    /**
     * 操作数据库的线程池
     */
    private ExecutorService mDBService;
    /**
     * 默认线程池，用于其他的操作
     */
    private ExecutorService mDefaultService;
    public static boolean hasShowLowSize=false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDBService = Executors.newSingleThreadExecutor();
        mDefaultService=Executors.newCachedThreadPool();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case TYPE_UPLOAD:
                        Bundle data = msg.getData();
                        String time=data.getString(ACTION_PARAM_TIME);
                        String id=data.getString(ACTION_PARAM_ID,System.nanoTime()+"");
                        processLogUpload(time,id);
                        break;
                    case TYPE_STORE_LOG:
                        if (SystemUtils.getRomAvailableSize()>MIN_SIZE_STORE&&!hasShowLowSize){
//                            Log.i("chen","开始上传文件：111111111111111111");
                            mDBService.execute(storeLog);
                        } else {
//                            Log.i("chen","开始上传文件：222222222222222222 hasShowLowSize:"+hasShowLowSize);
                            if(!hasShowLowSize) {
                                hasShowLowSize = true;
                                Toast.makeText(LogService.this,getString(R.string.low_size),Toast.LENGTH_SHORT).show();
                            }
                            mDBService.execute(checkRunnable);
                        }
                        break;
                    case TYPE_DATABASE_CHECK:
                        mDBService.execute(checkRunnable);
                        break;
                    case TYPE_STORE_DEVICE_INFO:
                        mDBService.execute(storeDeviceInfo);
                        break;
                }
            }
        };
        //发送存储log的消息
        handler.sendEmptyMessageDelayed(TYPE_STORE_LOG,STORE_LOG_DELAY_TIME);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //当前的动作是需要上传日志文件
        if (intent!=null){
            if (ACTION_UPLOAD.equals(intent.getAction())){
                //发送需要上传log日志的msg
                Message msg=handler.obtainMessage();
                msg.what=TYPE_UPLOAD;
                msg.setData(intent.getExtras());
                handler.sendMessage(msg);
            }else if(ACTION_CHECK.equals(intent.getAction())){
                Message msg=handler.obtainMessage();
                msg.what=TYPE_DATABASE_CHECK;
                handler.sendMessage(msg);
            }else if(ACTION_STORE_DEVICE_INFO.equals(intent.getAction())){
                Message message = handler.obtainMessage();
                message.what=TYPE_STORE_DEVICE_INFO;
                handler.sendMessage(message);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 处理log存储的runnable
     */
    private Runnable storeLog=new Runnable() {
        @Override
        public void run() {
            LogDBManager.getInstance().checkLog(getLogFile());
            LogMsgManager.getInstance().storeLog();
            handler.sendEmptyMessageDelayed(TYPE_STORE_LOG,STORE_LOG_DELAY_TIME);
        }
    };

    /**
     * 处理log文件的上传
     * @param time
     * @param id 任务的id
     */
    private void processLogUpload(String time,String id){
        String path=getDatabasePath(LogDBHelper.DATABASE_NAME).getAbsolutePath();
        mDBService.execute(new UploadRunnable(time,path,id));
        mDefaultService.execute(new ChangeStateRunnable(id));
    }

    /**
     * 处理log上传的runnable
     */
    private class UploadRunnable implements Runnable{
        private String time;
        private String path;
        private String id;
        public UploadRunnable(String time, String path,String id){
            this.time = time;
            this.path = path;
            this.id   = id;
        }
        @Override
        public void run() {
            //先判断存储的路径是否存在，如果不存在的话需要创建这样的路径
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/dzcx/analysis/");
            if (!file.exists()) {
                file.mkdirs();
            }
            LogMsgManager.getInstance().addLogMsg(DeviceNetFlowInfoUtils.create(LogService.this));
            LogMsgManager.getInstance().storeLog();
            //上传
            upload(path, time, file.getAbsolutePath(), id);
        }
    }
    private class ChangeStateRunnable implements Runnable{
        private String taskId;

        public ChangeStateRunnable(String taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            String url = Analysis.getInstance().getAppProxy().getNetInfo().getStartUploadUrl();
            HttpUtils.sendPost(url, taskId);
        }
    }
    private File getLogFile(){
        return getDatabasePath("log_analysis.db");
    }
    private Runnable checkRunnable=new Runnable() {
        @Override
        public void run() {
            //检查数据库，删除过期数据
            LogDBManager.getInstance().checkLog(getLogFile());
        }
    };
    private Runnable storeDeviceInfo=new Runnable() {
        @Override
        public void run() {
            LogDBManager.getInstance().storeDeviceInfo();
        }
    };

    /**
     * 删除数据库中不合法的数据，保留合法的数据
     * @param time      查找的条件
     * @param path      数据的路径
     */
    private void getDestLogByDB(String time, String path){
        if (TextUtils.isEmpty(time)){
            return;
        }
        SQLiteDatabase database=null;
        try {
            database=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
            database.delete(LogDBHelper.TABLE_NAME,LogDBHelper.ROW_TIME+" not like '%1$s%'",new String[]{time});
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (database!=null){
                    database.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 上传log
     * @param path          数据库的路径
     * @param time          获取的是那天的数据，为空的话获取所有的文件
     * @param dirPath       备份的文件的路径
     * @param id            任务的id
     */
    private void upload(final String path, String time, String dirPath, String id){
        Log.i("chen","开始上传文件：-===================upload()");
        if (TextUtils.isEmpty(path)){
            return;
        }
        //上传文件的路径，在上传log之前需要把合法的数据保存到一个文件中，这个文件的类型根据useDB字段判断
        String uploadFilePath=null;
        //上传的是db文件，需要生成一个db文件，实际上就是原始db文件的一个备份，然后删除不合法的数据
        final File originFile=new File(path);
        File parentFile = originFile.getParentFile();
        File[] matchFiles=parentFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                Pattern pattern=Pattern.compile(LogService.REGEX_PATTERN);
                Matcher matcher = pattern.matcher(name);
                return matcher.matches()||name.equals(originFile.getName());
            }
        });
        if (matchFiles!=null&&matchFiles.length>1){
            Log.i("chen","开始压缩文件11");
            uploadFilePath= LogFileUtils.compressToZip(matchFiles,dirPath,null);
            Log.i("chen","开始压缩文件11:"+uploadFilePath);
        }else{
            //对原始的db文件做一个备份
            if (TextUtils.isEmpty(time)){
                uploadFilePath= LogFileUtils.compressToZip(path, dirPath);
            }else{
                uploadFilePath= LogFileUtils.copy(path, dirPath,time+".db");
                //如果备份成功的话就删除这个备份文件中不合法的数据
                if (!TextUtils.isEmpty(uploadFilePath)){
                    getDestLogByDB(time,uploadFilePath);
                    uploadFilePath= LogFileUtils.compressToZip(path);
                }
            }
        }
        if (TextUtils.isEmpty(uploadFilePath)){
            return ;
        }else{
            uploadFile(uploadFilePath,id);
        }
    }

    /**
     * 上传文件
     * @param uploadFilePath
     * @param id 任务的id
     */
    private void uploadFile(String uploadFilePath,String id) {
        //上传文件，需要改进
//        Log.i("chen","开始上传文件："+uploadFilePath+",taskid:"+id);
//        String[] paths=uploadFilePath.split("/");
//        String uploadUrl=Analysis.getInstance().getAppProxy().getNetInfo().getUploadUrl()+"?fileName="+paths[paths.length-1]+"&taskId="+id;
////        String uploadUrl=Analysis.getInstance().getAppProxy().getNetInfo().getUploadUrl()+"?fileName="+paths[paths.length-1]+"&taskId="+id;
//        Log.i("chen","上传的地址："+uploadUrl);
//        String s = HttpUtils.uploadFile(uploadUrl, uploadFilePath);
//        Log.i("chen","上传文件的结果是："+s);
//        //判断文件是否存在，存在的话就删除之前产生的文件
//        File file=new File(uploadFilePath);
//        if (file.exists()){
//            file.delete();
//        }
        Log.i("chen","开始上传文件："+uploadFilePath+",taskid:"+id);
        String[] paths=uploadFilePath.split("/");
        String uploadUrl= Analysis.getInstance().getAppProxy().getNetInfo().getUploadUrl()+"?fileName="+paths[paths.length-1]+"&taskId="+id;
        Log.i("chen","上传的地址："+uploadUrl);
        String s = HttpUtils.uploadFile(uploadUrl, uploadFilePath,id);
        Log.i("chen","上传文件的结果是："+s);
        //判断文件是否存在，存在的话就删除之前产生的文件
        File file=new File(uploadFilePath);
        if (file.exists()){
            file.delete();
        }

    }

}
