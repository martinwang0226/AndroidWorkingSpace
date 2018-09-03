package androidworkingspacemartin.androidworkingspace.aopLog.manager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;

import androidworkingspacemartin.androidworkingspace.aopLog.Handler.CrashHandler;
import androidworkingspacemartin.androidworkingspace.aopLog.Service.LogService;
import androidworkingspacemartin.androidworkingspace.aopLog.Util.HookUtils;
import androidworkingspacemartin.androidworkingspace.aopLog.Util.SystemUtils;
import androidworkingspacemartin.androidworkingspace.aopLog.helper.AppProxy;
import androidworkingspacemartin.androidworkingspace.aopLog.model.NetInfoModel;
import androidworkingspacemartin.androidworkingspace.aopLog.model.ProtectModel;

/**
 * Created by martinwang on 2017/9/14.
 */

public class Analysis {
    private static final String CONFIG_FILE_NAME="log_analysis";
    private static Analysis mAnalysis;
    private Context mContext;
    private int satelliteCount;
    private int phoneSignalStrength;
    private GpsStatus.Listener mGpsListener;
    private PhoneStateListener mPhoneListener;
    private AppProxy mAppProxy;
    private Handler handler=null;
    private Analysis(){
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (mContext!=null){
                    registeGpsStatusListener(mContext);
                }
            }
        };
    }
    public static Analysis getInstance(){
        if (mAnalysis==null){
            mAnalysis=new Analysis();
        }
        return mAnalysis;
    }
    public void check(Context context){
        if (context==null){
            return ;
        }
        if (mContext==null)  {
            mContext=context.getApplicationContext();
            initOtherService(mContext);
        }
    }
    /**
     * 初始化
     * @param context
     * @param appProxy
     */
    public void init(Context context,AppProxy appProxy) {
        initWithProtectService(context,appProxy,null);
    }

    /**
     * 初始化
     * @param context
     * @param appProxy
     * @param protectList       受保护的服务或者activity
     */
    public void initWithProtectService(Context context, AppProxy appProxy, ArrayList<ProtectModel> protectList){
        mContext=context.getApplicationContext();
        setState(mContext,true);
        initOtherService(mContext);
        this.mAppProxy=appProxy;
        if(mAppProxy!=null&&(mAppProxy.getStoreDeviceInfoJudgeUrl()==null||mAppProxy.getStoreDeviceInfoJudgeUrl().length==0)){
            storeDeviceInfo();
        }
        ProtectManager.init(mContext,protectList);
        HookUtils.hook();
        storeConfig(mContext,appProxy);
    }

    /**
     * 初始化其他的服务
     * @param context
     */
    private void initOtherService(Context context) {
        //初始化数据库
        LogDBManager.getInstance().init(context);
        //注册gps信号变化的监听器
        checkGPSPermission(context);
        //注册手机信号变化的监听器
        registePhoneStateListener(context);
        //设置全局的异常捕获
        CrashHandler.getInstance().init();
        //打开log的服务
        checkLog(context);
    }

    private void storeConfig(Context context,AppProxy proxy){
        if (context!=null&&proxy!=null){
            SharedPreferences preferences = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("token",proxy.getUserToken());
            String[] storeDeviceInfoJudgeUrl = proxy.getStoreDeviceInfoJudgeUrl();
            if (storeDeviceInfoJudgeUrl!=null&&storeDeviceInfoJudgeUrl.length!=0){
                StringBuilder builder=new StringBuilder();
                for (String item:storeDeviceInfoJudgeUrl){
                    builder.append(item);
                    builder.append(",");
                }
                builder.deleteCharAt(builder.length()-1);
                edit.putString("judge",builder.toString());
            }
            NetInfoModel netInfo = proxy.getNetInfo();
            if (netInfo != null) {
                edit.putString("start_url",netInfo.getStartUploadUrl());
                edit.putString("upload_url",netInfo.getUploadUrl());
            }
            edit.commit();
        }
    }
    private void checkLog(Context context) {
        Intent intent = new Intent(context, LogService.class);
        intent.setAction(LogService.ACTION_CHECK);
        context.startService(intent);
    }

    private void registePhoneStateListener(Context context){
        if (mPhoneListener==null){
            TelephonyManager manager = ( TelephonyManager )context.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneListener=new PhoneStateListener(){
                @Override
                public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                    super.onSignalStrengthsChanged(signalStrength);
                    // int astSignal = -113 + 2*signalStrength.getGsmSignalStrength();
                    phoneSignalStrength = signalStrength.getGsmSignalStrength();
                }
            };
            manager.listen(mPhoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }

    }

    /**
     * 注册gps的监听器
     * @param context
     */
    private void registeGpsStatusListener(Context context){

        if (mGpsListener==null){
            LocationManager manager= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            mGpsListener=new GpsStatus.Listener() {
                @Override
                public void onGpsStatusChanged(int event) {
                    if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS){
                        satelliteCount= 0;
                        //todo  空指针异常
                        if (mContext!=null)
                        {
                            LocationManager manager= (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                            if (manager!=null)
                            {
                                //Log.i("chen","*****已修正获取卫星失败的问题*******");
                                GpsStatus gpsStatus=null;
                                try{
                                    gpsStatus= manager.getGpsStatus(null);
                                }catch (Exception e){
                                    //Log.i("chen","获取卫星失败："+e.getMessage()+","+e.getClass());
                                }
                                if (gpsStatus!=null)
                                {
                                    int maxSatellites=gpsStatus.getMaxSatellites();
                                    Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();
                                    if (satellites!=null)
                                    {
                                        Iterator<GpsSatellite> iters = satellites.iterator();
                                        while (iters.hasNext() && satelliteCount <= maxSatellites)
                                        {
                                            GpsSatellite s =  iters.next();
                                            if(s.usedInFix())
                                            {
                                                satelliteCount++;
                                            }
                                        }
                                    }
                                }
                            }else
                            {
                                satelliteCount=-1;
                            }
                        }else{
                            satelliteCount=-1;
                        }
                    }
                }
            };
            manager.addGpsStatusListener(mGpsListener);
        }

    }
    private void checkGPSPermission(Context context) {
        int state = context.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid());
        if (state == PackageManager.PERMISSION_GRANTED) {
            registeGpsStatusListener(context);
        } else {
            waitForGpsPermission();
        }
    }
    private void waitForGpsPermission(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    if (mContext!=null){
                        int state = mContext.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid());
                        if (state == PackageManager.PERMISSION_GRANTED) {
                            handler.sendEmptyMessage(0);
                        }
                    }else{
                        break;
                    }
                }
            }
        }).start();
    }

    /**
     * 获取有效的卫星的个数
     * @return
     */
    public synchronized int getSatelliteCount(){
        return satelliteCount;
    }
    public synchronized int getPhoneSignalStrength(){
        return phoneSignalStrength;
    }
    public AppProxy getAppProxy(){
        if (mAppProxy==null){
            restoreAppProxy();
        }
        return mAppProxy;
    }
    private void restoreAppProxy(){
        if (mContext==null){
            return ;
        }
        SharedPreferences preferences = mContext.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        final String token  = preferences.getString("token", "");
        String   judgeUrl   = preferences.getString("judge",null);
        String[] judgeArray = null;
        if (!TextUtils.isEmpty(judgeUrl)){
            judgeArray=judgeUrl.split(",");
        }
        String startUrl  = preferences.getString("start_url","");
        String uploadUrl = preferences.getString("upload_url","");
        final String[] urls=judgeArray;
        final NetInfoModel model=NetInfoModel.Builder.newInstance().startUploadUrl(startUrl).uploadUrl(uploadUrl).build();
        mAppProxy=new AppProxy() {
            @Override
            public String getUserToken() {
                return token;
            }

            @Override
            public String[] getStoreDeviceInfoJudgeUrl() {
                return urls;
            }

            @Override
            public NetInfoModel getNetInfo() {
                return model;
            }
        };
    }
    /**
     * 保存设备信息
     */
    public void storeDeviceInfo(){
        if (mContext==null){
            return ;
        }
        Intent intent=new Intent(mContext,LogService.class);
        intent.setAction(LogService.ACTION_STORE_DEVICE_INFO);
        mContext.startService(intent);
    }
    public boolean shouldStoreDeviceInfo(String url){
        AppProxy appProxy = getAppProxy();
        String[] storeDeviceInfoJudgeUrl = appProxy.getStoreDeviceInfoJudgeUrl();
        if (storeDeviceInfoJudgeUrl==null||storeDeviceInfoJudgeUrl.length==0){
            return false;
        }
        for (String item:storeDeviceInfoJudgeUrl){
            if (url.toLowerCase().contains(item.toLowerCase())){
                return true;
            }
        }
        return false;
    }
    public long getServerTime(){
        if (mContext==null){
            return System.currentTimeMillis();
        }else{
            SharedPreferences preferences = mContext.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
            long serverTime = preferences.getLong("server_time", System.currentTimeMillis());
            return serverTime;
        }
    }
    public void saveServerTime(long serverTime){
        if (mContext==null){
            return ;
        }
        SharedPreferences preferences = mContext.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong("server_time",serverTime);
        edit.apply();
    }

    /**
     * 设置当前保活的状态
     * @param isAlive
     */
    public void setState(Context context,boolean isAlive){
        LogService.hasShowLowSize=false;
        Context applicationContext=context.getApplicationContext();
        SharedPreferences preferences=applicationContext.getSharedPreferences(CONFIG_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("protect_service",isAlive);
        edit.apply();
    }

    public void stopService(Context context){
        Context applicationContext=context.getApplicationContext();
        setState(context,false);
        ProtectManager.stop(applicationContext);
        //停止极光推送
        SystemUtils.stopPush(applicationContext);


    }

    /**
     * 判断保活的服务是否开启
     * @param context
     * @return
     */
    public boolean isProtectEnable(Context context){
        Context applicationContext=null;
        if (context!=null){
            applicationContext=context.getApplicationContext();
        }else if(mContext!=null){
            applicationContext=mContext;
        }
        if (applicationContext==null){
            return true;
        }else{
            SharedPreferences preferences = applicationContext.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
            return preferences.getBoolean("protect_service",true);
        }
    }

}