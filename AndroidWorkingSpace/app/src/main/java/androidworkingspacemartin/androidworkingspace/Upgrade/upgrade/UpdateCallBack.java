package androidworkingspacemartin.androidworkingspace.Upgrade.upgrade;

/**
 * Created by martinwang on 2017/8/25.
 */

public interface UpdateCallBack {
    void suggestUpdateCreate();

    void suggestUpdateClose();

    void suggestUpdate();

    void constraintUpdateCreate();

    void constraintUpdateClose();

    void constraintUpdate();
}
