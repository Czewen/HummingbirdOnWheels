package hummingbird.android.mobile_app.events;

/**
 * Created by CzeWen on 2016-01-03.
 */
public class NetworkFailureEvent {
    String error;

    public NetworkFailureEvent(String error){
        this.error = error;
    }

    public String getError(){
        return error;
    }

}
