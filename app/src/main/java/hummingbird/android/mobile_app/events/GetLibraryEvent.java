package hummingbird.android.mobile_app.events;

/**
 * Created by CzeWen on 2016-01-08.
 */
public class GetLibraryEvent {

    private String auth_token;
    public String username;

    public GetLibraryEvent(String username, String auth_token){
        this.auth_token = auth_token;
        this.username = username;
    }

    public GetLibraryEvent(String username){
        this.username = username;
    }

    public String getAuth_token(){
        return auth_token;
    }

    public String getUsername(){
        return username;
    }
}
