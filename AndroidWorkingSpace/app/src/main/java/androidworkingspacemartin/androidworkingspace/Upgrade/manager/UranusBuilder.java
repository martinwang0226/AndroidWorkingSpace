package androidworkingspacemartin.androidworkingspace.Upgrade.manager;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.tencent.UranusDApplication;
import androidworkingspacemartin.androidworkingspace.Upgrade.upgrade.UpdateCallBack;

/**
 * Created by martinwang on 2017/6/12.
 */

public class UranusBuilder {

    public List<Class<? extends Activity>> canNotShowUpgradeActs;
    public String buglyId;
    public String channel;
    public String targetAppVersion;
    public boolean isDebug;
    public boolean isDevelopmentDevice;

    public boolean queryNow;
    public boolean isAutoQuery;
    public boolean isForceRestart;

    public boolean tinkerEnable;
    public boolean hotfixEnable;

    public UranusDApplication uranusDApplication;
    public UpdateCallBack updataCallBack;

    private UranusBuilder(Builder builder) {
        canNotShowUpgradeActs = builder.canNotShowUpgradeActs;
        buglyId = builder.buglyId;
        channel = builder.channel;
        targetAppVersion = builder.targetAppVersion;
        isDebug = builder.isDebug;
        isDevelopmentDevice = builder.isDevelopmentDevice;
        tinkerEnable = builder.tinkerEnable;
        hotfixEnable = builder.hotfixEnable;
        queryNow = builder.queryNow;
        isAutoQuery = builder.isAutoQuery;
        isForceRestart = builder.isForceRestart;
        uranusDApplication = builder.uranusDApplication;
        updataCallBack = builder.updataCallBack;
    }

    public static final class Builder {
        private List<Class<? extends Activity>> canNotShowUpgradeActs;
        private String buglyId;
        private String channel;
        private String targetAppVersion;
        private boolean isDebug;
        private boolean isDevelopmentDevice;

        private boolean tinkerEnable = true;
        private boolean hotfixEnable = false;

        private boolean isForceRestart = false;

        private boolean queryNow = true;
        private boolean isAutoQuery = true;

        private UranusDApplication uranusDApplication;
        private UpdateCallBack updataCallBack;

        public Builder() {
        }

        public Builder addCanNotShowUpgradeAct(Class<? extends Activity>... val) {
            if (val == null) {
                return this;
            }
            if (canNotShowUpgradeActs == null) {
                canNotShowUpgradeActs = new ArrayList<>();
            }
            for (Class _cls : val) {
                canNotShowUpgradeActs.add(_cls);
            }
            return this;
        }

        public Builder buglyId(String val) {
            buglyId = val;
            return this;
        }

        public Builder channel(String val) {
            channel = val;
            return this;
        }

        public Builder targetAppVersion(String val) {
            targetAppVersion = val;
            return this;
        }

        public Builder isDebug(boolean val) {
            isDebug = val;
            return this;
        }

        public Builder isDevelopmentDevice(boolean val) {
            isDevelopmentDevice = val;
            return this;
        }

        public Builder setTinkerEnable(boolean tinkerEnable) {
            this.tinkerEnable = tinkerEnable;
            return this;
        }

        public Builder setHotfixEnable(boolean hotfixEnable) {
            this.hotfixEnable = hotfixEnable;
            return this;
        }

        public Builder setAutoQuery(boolean autoQuery) {
            this.isAutoQuery = autoQuery;
            return this;
        }

        public Builder setForceRestart(boolean forceRestart) {
            isForceRestart = forceRestart;
            return this;
        }

        public Builder setUranusDApplication(UranusDApplication uranusDApplication, boolean autoQuery) {
            this.uranusDApplication = uranusDApplication;
            this.isAutoQuery = autoQuery;
            return this;
        }

        public Builder setUpdataCallBack(UpdateCallBack callBack) {
            this.updataCallBack = callBack;
            return this;
        }

        public UranusBuilder build() {
            return new UranusBuilder(this);
        }
    }
}