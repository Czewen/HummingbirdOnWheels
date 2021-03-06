package hummingbird.android.mobile_app.presenters;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.services.LibraryService;
import hummingbird.android.mobile_app.events.GetLibraryEvent;
import hummingbird.android.mobile_app.events.GetLibrarySuccessEvent;
import hummingbird.android.mobile_app.models.LibraryEntry;
import hummingbird.android.mobile_app.views.activities.LibraryView;
import hummingbird.android.mobile_app.views.fragments.LibraryListFragment;
import retrofit.Retrofit;

/**
 * Created by CzeWen on 2016-01-21.
 */
public class LibraryPresenter extends Presenter {

    LibraryService library_service;
    LibraryView view;
    ArrayList<LibraryEntry> library_entries;

    public LibraryPresenter(LibraryView view){
        this.view = view;
        library_service = new LibraryService();
        EventBus.getDefault().register(this);
        super.bindService(library_service);
    }

    //Make an api call only if we dont have any saved information
    public void fetchLibraryInformation(String username){
        if(library_entries!=null){
            populateLibrary();
        }
        else{
            EventBus.getDefault().post(new GetLibraryEvent(username));
        }
    }

    public void fetchLibraryInformation(String username, String auth_token){
        if(library_entries!=null){
            populateLibrary();
        }
        else{
            EventBus.getDefault().post(new GetLibraryEvent(username, auth_token));
        }
    }


    public void onEvent(GetLibrarySuccessEvent event){
        library_entries = event.getLibraryEntries();
        populateLibrary();
    }


    public void populateLibrary(){
        LibraryListFragment library_list_view = view.getCurrentFragment();
        String list_type = library_list_view.getListType();
        Bundle args = new Bundle();
        if(library_entries.size()>0) {
            if (list_type.contentEquals("All")) {
                args.putParcelableArrayList("ARG_ENTRIES", library_entries);
            } else {
                ArrayList<LibraryEntry> matching_entries = new ArrayList<>();
                for (LibraryEntry entry : library_entries) {
                    if (mapJsonResultToListType(entry.status).contentEquals(list_type)) {
                        matching_entries.add(entry);
                    }
                }
                args.putParcelableArrayList("ARG_ENTRIES", matching_entries);
            }
            library_list_view.populateList(args);
            library_list_view.setNeeds_update(false);
        }
    }

    public String mapJsonResultToListType(String json_result){
        String mapped_result;
        switch(json_result){
            case "currently-watching":
                return "Watching";
            case "plan-to-watch":
                return "Plan to Watch";
            case "completed":
                return "Completed";
            case "dropped":
                return "Dropped";
            case "on-hold":
                return "On hold";
        }
        return json_result + "is not mappable to list type";
    }


    public void updateOldLibraryEntry(int id, HashMap<String, String> changes){
        for(LibraryEntry library_entry : library_entries){
            if(library_entry.id == id) {
                for (Map.Entry<String, String> entry : changes.entrySet())
                    switch (entry.getKey()) {
                        case "status":
                            String status = entry.getValue();
                            library_entry.status = status;
                            //list with matching watch status needs to add this entry
                            LibraryListFragment fragment = view.getFragment(mapJsonResultToListType(status));
                            //null is returned if the fragment has not been instantiated yet, in that case we don't need to call for an update
                            //as it'll fetch for fresh values on creation. Only call for an update on old fragments.
                            if(fragment!=null)
                                fragment.setNeeds_update(true);
                            break;
                        case "episodes_watched":
                            library_entry.episodes_watched = Integer.parseInt(entry.getValue());
                            break;
                        case "rating":
                            library_entry.rating.value = Float.parseFloat(entry.getValue());
                            break;
                    }
                break;
            }
        }
    }

}
