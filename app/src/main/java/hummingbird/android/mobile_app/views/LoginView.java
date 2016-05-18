package hummingbird.android.mobile_app.views;
import android.view.View;

import de.greenrobot.event.EventBus;

/**
 * Created by CzeWen on 2015-11-30.
 */
public interface LoginView {

    public void login(View view);

    public void setAuthToken(String auth_token);

    public void loginSuccess(String username, String auth_token);

    public void displayLoginFailed(String reason);

}
