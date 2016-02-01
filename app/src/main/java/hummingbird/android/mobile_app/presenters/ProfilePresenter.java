package hummingbird.android.mobile_app.presenters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.services.UserProfileService;
import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.events.GetLibraryEvent;
import hummingbird.android.mobile_app.events.GetLibrarySuccessEvent;
import hummingbird.android.mobile_app.events.GetProfileSuccessEvent;
import hummingbird.android.mobile_app.events.GetUserProfileEvent;
import hummingbird.android.mobile_app.events.NetworkFailureEvent;
import hummingbird.android.mobile_app.events.ResponseFailureEvent;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.views.ProfileView;
import hummingbird.android.mobile_app.models.User;
import hummingbird.android.mobile_app.models.LibraryEntry;
import hummingbird.android.mobile_app.views.fragments.LibraryListFragment;
import hummingbird.android.mobile_app.views.fragments.ProfileFragmentPagerAdapter;
import retrofit.Response;

import java.util.ArrayList;
/**
 * Created by CzeWen on 2016-01-03.
 */
public class ProfilePresenter extends Presenter{

    UserProfileService user_profile_service;
    ProfileView profile_view;
    LibraryAdapter library_adapter;
    ArrayList<LibraryEntry> library_entries;
    String current_user_profile_name;


    public ProfilePresenter(ProfileView profile_view){
        user_profile_service = new UserProfileService();
        this.profile_view = profile_view;
        current_user_profile_name = profile_view.getCurrentUser();
        EventBus.getDefault().register(this);
        super.bindService(user_profile_service);
        //library_entries = new ArrayList<LibraryEntry>();//https://static.hummingbird.me/anime/poster_images/000/000/359/large/461736.jpg?1409535570
        //LibraryEntry test_entry = new LibraryEntry();
        //test_entry.anime = new Anime();
        //test_entry.anime.title = "test";
        //test_entry.anime.cover_image =  "https://static.hummingbird.me/anime/poster_images/000/000/359/large/461736.jpg?1409535570";
    }

    public void refresh(){

    }

    public void getProfileInfo(String username){
        EventBus.getDefault().post(new GetUserProfileEvent(username));
    }

    public void getOwnInfo(String auth_token){
        EventBus.getDefault().post(new GetUserProfileEvent("me", auth_token));
    }

    public void getOwnLibrary(String auth_token){
        //profile_view.setResponseError("Calling API for libraries");
        EventBus.getDefault().post(new GetLibraryEvent("me", auth_token));
    }

    public void updateProfile(User profile_information){
        profile_view.setName(profile_information.getName());
        //profile_view.setBio(profile_information.getBio());
        if(profile_information.getAvatar() != null || !profile_information.getAvatar().contentEquals("")){
            profile_view.setProfile_Avatar(profile_information.getAvatar());
        }
        if(profile_information.getCover_image()!= null || !profile_information.getCover_image().contentEquals("")){
            profile_view.setCover_image(profile_information.getCover_image());
        }
    }


    public void onEvent(GetProfileSuccessEvent event){
        updateProfile(event.getUser_profile_information());
    }

    public void onEvent(GetLibrarySuccessEvent event){
        library_entries = event.getLibraryEntries();
        populateLibrary(library_entries);
    }

    public void populateLibrary(ArrayList<LibraryEntry> library_entries){
        Fragment fragment = profile_view.getCurrentFragment();
        if(fragment.getClass().equals(LibraryListFragment.class)){
            LibraryListFragment list_fragment = (LibraryListFragment) fragment;
            Bundle parcelable_entries = new Bundle();
            parcelable_entries.putParcelableArrayList("ARG_ENTRIES", library_entries);
            list_fragment.populateList(parcelable_entries);
            //profile_view.setResponseError("went through populate list for fragment");
        }
    }

    public void onEvent(ResponseFailureEvent event){
        profile_view.setResponseError(event.getError());
        //profile_view.setResponseError("RESPONSE FAILURE");
    }

    public void onEvent(NetworkFailureEvent event){
        profile_view.setResponseError("NETWORK FAILURE");
    }

    public boolean isListening(){
        return EventBus.getDefault().isRegistered(this);
    }

    public void bindView(ProfileView view){
        this.profile_view =  view;
    }

    public void unbindView(){
        this.profile_view = null;
    }

}
