package hummingbird.android.mobile_app.events;

/**
 * Created by CzeWen on 2016-01-02.
 */
public class GetUserProfileEvent {

    private String user_profile_name;
    private String auth_token;

    public GetUserProfileEvent(String user_profile_name){
        this.user_profile_name = user_profile_name;
    }

    public GetUserProfileEvent(String user_profile_name, String auth_token){
        this.user_profile_name = user_profile_name;
        this.auth_token = auth_token;
    }

    public String getUser_profile_name(){
        return user_profile_name;
    }

    public String getAuth_token(){
        return auth_token;
    }


}
