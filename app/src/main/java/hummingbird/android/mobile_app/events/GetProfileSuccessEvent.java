package hummingbird.android.mobile_app.events;
import hummingbird.android.mobile_app.models.User;
/**
 * Created by CzeWen on 2016-01-03.
 */
public class GetProfileSuccessEvent {

    private User profile_information;

    public GetProfileSuccessEvent(User profile_information){
        this.profile_information =  profile_information;
    }

    public User getUser_profile_information(){
    return profile_information;
    }
}
