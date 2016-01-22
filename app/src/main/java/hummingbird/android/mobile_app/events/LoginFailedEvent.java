package hummingbird.android.mobile_app.events;

/**
 * Created by CzeWen on 2015-12-21.
 */
public class LoginFailedEvent {

    public final String error;

    public LoginFailedEvent(String error){
        this.error =  error;
    }

    public String getError(){
        return error;
    }
}
