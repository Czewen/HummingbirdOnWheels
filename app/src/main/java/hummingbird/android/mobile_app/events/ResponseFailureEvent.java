package hummingbird.android.mobile_app.events;

/**
 * Created by CzeWen on 2016-01-03.
 */
public class ResponseFailureEvent {

    private int http_error_code;
    private String failure_message;

    public ResponseFailureEvent(int http_error_code, String failure_message){
        this.http_error_code = http_error_code;
        this.failure_message = failure_message;
    }

    public String getFailure_message(){
        return failure_message;
    }

    public int getHttp_error_code(){
        return http_error_code;
    }

    public String getError(){
        return http_error_code+ ": "+failure_message;
    }
}
