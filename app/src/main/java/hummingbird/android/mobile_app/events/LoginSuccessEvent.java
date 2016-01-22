package hummingbird.android.mobile_app.events;

/**
 * Created by CzeWen on 2015-12-21.
 */
public class LoginSuccessEvent {

    public final String oauth_token;
    public final String username;


    public LoginSuccessEvent(String username, String oauth_token){
        this.oauth_token = oauth_token;
        this.username = username;
    }

    public String getOauth_token(){
        return oauth_token;
    }

    public String getUsername(){return username;}
}
