package hummingbird.android.mobile_app.events;

import hummingbird.android.mobile_app.models.LibraryEntry;

/**
 * Created by CzeWen on 2016-04-27.
 */
public class UpdateLibrarySuccessEvent {
    LibraryEntry response;
    String update_type;
    public UpdateLibrarySuccessEvent(String update_type, LibraryEntry response){
        this.update_type = update_type;
        this.response = response;
    }

    public String getUpdate_type(){
        return update_type;
    }

    public LibraryEntry getResponse(){
        return response;
    }
}
