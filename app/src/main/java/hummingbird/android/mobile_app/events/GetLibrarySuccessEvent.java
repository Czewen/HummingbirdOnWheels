package hummingbird.android.mobile_app.events;

import hummingbird.android.mobile_app.models.LibraryEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
/**
 * Created by CzeWen on 2016-01-09.
 */
public class GetLibrarySuccessEvent {
    ArrayList<LibraryEntry> library_entries;

    public GetLibrarySuccessEvent(ArrayList<LibraryEntry> library_entries){
        this.library_entries = library_entries;
    }

    public ArrayList<LibraryEntry> getLibraryEntries(){
        return library_entries;
    }

}
