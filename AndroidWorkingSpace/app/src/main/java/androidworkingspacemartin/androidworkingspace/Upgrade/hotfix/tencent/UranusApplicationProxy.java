package androidworkingspacemartin.androidworkingspace.Upgrade.hotfix.tencent;


import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * .-""""-.        .-""""-.
 * /        \      /        \
 * /_        _\    /_        _\
 * // \      / \\  // \      / \\
 * |\__\    /__/|  |\__\    /__/|
 * \    ||    /    \    ||    /
 * \        /      \        /
 * \  __  /        \  __  /
 * '.__.'          '.__.'
 * |  |            |  |
 * |  |            |  |
 *
 * @see https://bugly.qq.com/docs/user-guide/instruction-manual-android-hotfix/?v=20170307182353
 * {@link DApplication}
 */
public class UranusApplicationProxy extends TinkerApplication {

    public UranusApplicationProxy() {
        super(ShareConstants.TINKER_ENABLE_ALL, BuildConfig.NAME_CLASS_APPLICATION, "com.tencent.tinker.loader.TinkerLoader", false);
    }

    /**
     *
     * @param tinkerFlags
     * @param delegateClassName
     * @param loaderClassName
     * @param tinkerLoadVerifyFlag
     * @{@<code>super(ShareConstants.TINKER_ENABLE_ALL, "com.visionet.dazhongcx_ckd.DApplication", "com.tencent.tinker.loader.TinkerLoader", false);</code>}
     */
    protected UranusApplicationProxy(int tinkerFlags, String delegateClassName, String loaderClassName, boolean tinkerLoadVerifyFlag) {
        super(tinkerFlags, delegateClassName, loaderClassName, tinkerLoadVerifyFlag);
    }
}
