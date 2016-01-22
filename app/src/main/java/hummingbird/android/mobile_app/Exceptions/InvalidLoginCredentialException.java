package hummingbird.android.mobile_app.Exceptions;

/**
 * Created by CzeWen on 2015-12-01.
 */
public class InvalidLoginCredentialException extends Exception {
    String error_message;
    public InvalidLoginCredentialException(String error){
        super();
        error_message = error;
    }

    public String returnError(){
        return error_message;
    }
}
