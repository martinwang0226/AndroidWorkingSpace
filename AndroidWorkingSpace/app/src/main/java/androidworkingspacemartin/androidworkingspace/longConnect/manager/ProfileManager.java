package androidworkingspacemartin.androidworkingspace.longConnect.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidworkingspacemartin.androidworkingspace.longConnect.ProfileModel;

/**
 * Created by martinwang on 2017/8/8.
 */

public class ProfileManager {
    private static final String PROFILE_CONFIG    = "profile_config";
    public static final String PROFILE_INFO       = "profile_info";
    public static final String PROFILE_CAR_NUMBER = "car_number";
    private static ProfileManager mProfileManager;
    private ProfileModel mProfileModel;
    private SharedPreferences mSharedProference;

    private ProfileManager(Context applicationContext){
        initSharedPreference(applicationContext);
        initProfileModel();
    }

    public static void init(Context applicationContext) {
        synchronized (ProfileManager.class) {
            if (null == mProfileManager) {
                applicationContext = applicationContext.getApplicationContext();
                mProfileManager    = new ProfileManager(applicationContext);
            }
        }
    }

    public static ProfileManager getInstance() {
        if (null == mProfileManager) {
            throw new IllegalStateException("please init first");
        }
        return mProfileManager;
    }

    private void initProfileModel() {
        String profileInfo = getString(PROFILE_INFO);
        mProfileModel      = ProfileModel.create(profileInfo);
    }

    private SharedPreferences initSharedPreference(Context applicationContext){
        if (null == mSharedProference) {
            mSharedProference = applicationContext.getSharedPreferences(PROFILE_CONFIG, Context.MODE_PRIVATE);
        }
        return mSharedProference;
    }

    private String getString(String name){
        String string = mSharedProference.getString(name,null);
        return string;
    }

    public void addDisconnectCount(){
        mProfileModel.addDisconnectCount();
        synchronize();
    }

    public void addReconnectCount(){
        mProfileModel.addReconnectCount();
        synchronize();
    }

    public void addSendCount(){
        mProfileModel.addSendCount();
        synchronize();
    }

    public void addSuccessCount(){
        mProfileModel.addSuccessCount();
        synchronize();
    }

    public void synchronize(){
        String storeInfo = mProfileModel.toJson();
        new SharedPreferencesSync(mSharedProference).execute(PROFILE_CONFIG, storeInfo);
    }

    public void saveCarNum(String carNum) {
        new SharedPreferencesSync(mSharedProference).execute(PROFILE_CAR_NUMBER, carNum);
    }

    public ProfileModel getAnalysisInfo() {
        return mProfileModel;
    }

    public String getCarNum() {
        return getString(PROFILE_CAR_NUMBER);
    }
}
