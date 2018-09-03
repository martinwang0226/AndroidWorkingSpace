package androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.ali;

import android.content.Context;

import androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.BaseFix;

/**
 * 1.服务器清除补丁，本地需要重启生效
 *
 * @see http://baichuan.taobao.com/docs/doc.htm?spm=a3c0d.7629140.0.0.EDWJIi&treeId=234&articleId=106531&docType=1
 */
public class AliFix extends BaseFix {

    private AliFix() {
    }

    public static AliFix getInstance() {
        return AliFix.LazyHolder.strategy;
    }

    private static class LazyHolder {
        public static final AliFix strategy = new AliFix();
    }

    @Override
    public void init() {

    }

    public void init(Context ctx, String targetAppVersion) {
//        SophixManager.getInstance().setContext((Application) ctx.getApplicationContext())
//                .setAppVersion(targetAppVersion)
//                .setAesKey(null)
//                .setEnableDebug(true)
//                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
//                    /**
//                     * 阿里补丁回调
//                     * @param mode 补丁模式, 0:正常请求模式 1:扫码模式 2:本地补丁模式
//                     * @param code 补丁加载状态码, 详情查看PatchStatusCode类说明 {@link PatchStatus}
//                    public static final int CODE_REQ_START = 0;
//                    public static final String INFO_REQ_START = "ready to start.";
//                    public static final int CODE_LOAD_SUCCESS = 1;
//                    public static final String INFO_LOAD_SUCCESS = "load new patch success.";
//                    public static final int CODE_ERR_NOTINIT = 2;
//                    public static final String INFO_ERR_NOTINIT = "didn\'t initialize hotfix sdk or initialize fail.";
//                    public static final int CODE_ERR_NOTMAIN = 3;
//                    public static final String INFO_ERR_NOTMAIN = "only allow query in main process.";
//                    public static final int CODE_ERR_INBLACKLIST = 4;
//                    public static final String INFO_ERR_INBLACKLIST = "current device does\'t support hotfix.";
//                    public static final int CODE_REQ_ERR = 5;
//                    public static final String INFO_REQ_ERR = "pull patch info detail fail, please check log.";
//                    public static final int CODE_REQ_NOUPDATE = 6;
//                    public static final String INFO_REQ_NOUPDATE = "there is not update.";
//                    public static final int CODE_REQ_NOTNEWEST = 7;
//                    public static final String INFO_REQ_NOTNEWEST = "the query patchversion equals or less than current patchversion, stop download.";
//                    public static final int CODE_DOWNLOAD_FAIL = 8;
//                    public static final int CODE_DOWNLOAD_SUCCESS = 9;
//                    public static final String INFO_DOWNLOAD_SUCCESS = "patch download success.";
//                    public static final int CODE_DOWNLOAD_BROKEN = 10;
//                    public static final String INFO_DOWNLOAD_BROKEN = "patch file is broken.";
//                    public static final int CODE_UNZIP_FAIL = 11;
//                    public static final String INFO_UNZIP_FAIL = "unzip patch file error, please check value of AndroidMenifest.xml RSASECRET or initialize param aesKey.";
//                    public static final int CODE_LOAD_RELAUNCH = 12;
//                    public static final String INFO_LOAD_RELAUNCH = "please relaunch app to load new patch.";
//                    public static final int CODE_LOAD_FAIL = 13;
//                    public static final String INFO_LOAD_FAIL = "load patch fail, please check stack trace of an exception: ";
//                    public static final int CODE_LOAD_NOPATCH = 14;
//                    public static final String INFO_LOAD_NOPATCH = "not found any patch file to load.";
//                    public static final int CODE_REQ_APPIDERR = 15;
//                    public static final String INFO_REQ_APPIDERR = "appid is not found.";
//                    public static final int CODE_REQ_SIGNERR = 16;
//                    public static final String INFO_REQ_SIGNERR = "token is invaild, please check APPSECRET.";
//                    public static final int CODE_REQ_UNAVAIABLE = 17;
//                    public static final String INFO_REQ_UNAVAIABLE = "req is unavailable as has already been in arrearage.";
//                    public static final int CODE_REQ_CLEARPATCH = 18;
//                    public static final String INFO_REQ_CLEARPATCH = "clean client patch as server publish clear cmd.";
//                    public static final int CODE_REQ_TOOFAST = 19;
//                    public static final String INFO_REQ_TOOFAST = "two consecutive request should not short then 3s.";
//                    public static final int CODE_PATCH_INVAILD = 20;
//                    public static final String INFO_PATCH_INVAILD = "patch invaild, as patch not exist or is dir or not a jar compress file.";
//                    public static final String REPORT_DOWNLOAD_SUCCESS = "100";
//                    public static final String REPORT_DOWNLOAD_ERROR = "101";
//                    public static final String REPORT_LOAD_SUCCESS = "200";
//                    public static final String REPORT_LOAD_ERROR = "201";
//                     * @param info 补丁加载详细说明, 详情查看PatchStatusCode类说明
//                     * @param handlePatchVersion 当前处理的补丁版本号, 0:无 -1:本地补丁 其它:后台补丁
//                     */
//                    @Override
//                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
//                        if (Uranus.isDebug) {
//                            Log.d(Uranus.TAG, "mode:" + mode + "  code:" + code + "  info:" + info + "  handlePatchVersion:" + handlePatchVersion);
//                        }
//                        /**
//                         * 常用code
//                         code: 1 补丁加载成功
//                         code: 6 服务端没有最新可用的补丁
//                         code: 11 RSASECRET错误，官网中的密钥是否正确请检查
//                         code: 12 当前应用已经存在一个旧补丁, 应用重启尝试加载新补丁
//                         code: 13 补丁加载失败, 导致的原因很多种, 比如UnsatisfiedLinkError等异常, 此时应该严格检查logcat异常日志
//                         code: 16 APPSECRET错误，官网中的密钥是否正确请检查
//                         code: 18 一键清除补丁
//                         code: 19 连续两次queryAndLoadNewPatch()方法调用不能短于3s
//                         */
//                        // 补丁加载回调通知
//                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
//                            // 表明补丁加载成功
//                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
//                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
//                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
//
//                        }
//                        /**
//                         * load patch fail, please check stack trace of an exception:
//                         * 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
//                         */
//                        else if (code == PatchStatus.CODE_LOAD_FAIL) {
//                            SophixManager.getInstance().cleanPatches();
//                        }
//                        /**
//                         * clean client patch as server publish clear cmd.
//                         */
//                        else if (code == PatchStatus.CODE_REQ_CLEARPATCH) {
//                            SophixManager.getInstance().cleanPatches();
//                        } else {
//                            // 其它错误信息, 查看PatchStatus类说明
//                        }
//                    }
//                }).initialize();
    }

    @Override
    public void down() {

    }

    @Override
    public void apply() {

    }

    @Override
    public void query() {
//        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    @Override
    public void clean() {
//        SophixManager.getInstance().cleanPatches();
    }
}
