package hummingbird.android.mobile_app.events;
import retrofit.Response;
/**
 * Created by CzeWen on 2015-12-21.
 */
public class LoginEvent {

    public final String username_or_email;
    public final String password;


    public LoginEvent(String username_or_email, String password){
        this.username_or_email = username_or_email;
        this.password = password;
    }

    public String getLoginName(){
        return username_or_email;
    }

    public java.lang.String getPassword() {
        return password;
    }
}
