package hummingbird.android.mobile_app.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.services.AnimeService;
import hummingbird.android.mobile_app.Api.services.LibraryService;
import hummingbird.android.mobile_app.events.GetAnimeEvent;
import hummingbird.android.mobile_app.events.GetAnimeSuccessEvent;
import hummingbird.android.mobile_app.events.UpdateLibraryEvent;
import hummingbird.android.mobile_app.events.UpdateLibrarySuccessEvent;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.models.LibraryEntry;
import hummingbird.android.mobile_app.views.activities.AnimeView;

/**
 * Created by CzeWen on 2016-01-18.
 */
public class AnimePresenter extends Presenter {
    AnimeView view;
    AnimeService anime_service;
    LibraryService library_service;
    Anime anime;
    LibraryEntry library_entry;
    boolean is_library_entry = false;
    ArrayList<String> watch_status_index_mapping = new ArrayList<>();
    HashMap<String, String> successful_updates = new HashMap<>();

    int library_entry_position;
    int total_updates = 0;

    public AnimePresenter(AnimeView view){
        this.view = view;
        anime_service = new AnimeService();
        library_service = new LibraryService();
        EventBus.getDefault().register(this);
        super.bindService(anime_service);
        super.bindService(library_service);
        setWatch_status_index_mapping();

    }

    public void bindView(AnimeView view){
        this.view = view;
    }

    public void fetchAnime(int anime_id){
        EventBus.getDefault().post(new GetAnimeEvent(anime_id));
    }

    public void onEvent(GetAnimeSuccessEvent event){
        anime = event.getAnimeObject();
        view.setCoverPhoto(anime.cover_image);
        view.setTitle(anime.title);
        if(is_library_entry) {
            view.setEpisodesWatched(library_entry.episodes_watched);
            view.setWatchStatus(watch_status_index_mapping.indexOf(library_entry.status));
        }
        view.setShowType(anime.show_type);
        view.setShowStatus(anime.status);
        view.setEpisodeCount(anime.episode_count);

    }

    public void onEvent(UpdateLibrarySuccessEvent event){
        String update_type = event.getUpdate_type();
        total_updates++;
        switch(update_type){
            case "episodes_watched":
                int new_value = event.getResponse().episodes_watched;
                recordUpdate(update_type, Integer.toString(new_value));
                view.setEpisodesWatched(new_value);
                break;
            case "status":
                String status = event.getResponse().status;
                recordUpdate(update_type, status);
                int index = watch_status_index_mapping.indexOf(status);
                view.setWatchStatus(index);
                if(status.contentEquals("completed"))
                    view.setEpisodesWatched(anime.episode_count);
                break;
        }
    }

    public void setLibraryEntry(LibraryEntry entry){
        is_library_entry = true;
        library_entry = entry;
    }

    public void updateEpisodesWatched(String auth_token, int current_value){

        int new_value = current_value + 1;
        if(new_value>=anime.episode_count)
            return;
        sendLibraryUpdate(auth_token, "episodes_watched", Integer.toString(new_value));
    }

    public void updateWatchStatus(String auth_token, int watch_status_index){
        String value = watch_status_index_mapping.get(watch_status_index);
        sendLibraryUpdate(auth_token, "status", value);
    }

    public void sendLibraryUpdate(String auth_token, String update_type, String value){
        UpdateLibraryEvent event = new UpdateLibraryEvent(auth_token, anime.id);
        event.setUpdate_type(update_type);
        event.value = value;
        EventBus.getDefault().post(event);
    }

    public void setWatch_status_index_mapping(){
        watch_status_index_mapping.add(0,"currently-watching");
        watch_status_index_mapping.add(1, "completed");
        watch_status_index_mapping.add(2, "plan-to-watch" );
        watch_status_index_mapping.add(3, "dropped");
    }
    public void recordUpdate(String update_type, String value){
        //remove older updates
        //map should contain latest updates for each update_type
        if(successful_updates.containsKey(update_type))
            successful_updates.remove(update_type);
        successful_updates.put(update_type, value);
    }

    public HashMap<String, String> getSuccessfulUpdates(){
        return successful_updates;
    }

    public void setLibrary_entry_position(int position){
        library_entry_position = position;
    }

    public int getLibrary_entry_position(){
        return library_entry_position;
    }

    public boolean hasUpdates(){
        return !(total_updates == 0);
    }
}
