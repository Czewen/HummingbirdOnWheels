package hummingbird.android.mobile_app.presenters;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

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
        view.setEpisodeCount(anime.episode_count);
        if(is_library_entry) {
            view.setEpisodesWatched(library_entry.episodes_watched);
            view.setWatchStatus(watch_status_index_mapping.indexOf(library_entry.status));
        }
    }

    public void onEvent(UpdateLibrarySuccessEvent event){
        switch(event.getUpdate_type()){
            case "episodes_watched":
                view.setEpisodesWatched(event.getResponse().episodes_watched);
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
        UpdateLibraryEvent event = new UpdateLibraryEvent(auth_token, anime.id);
        event.setUpdate_type("episodes_watched");
        event.value = Integer.toString(new_value);
        EventBus.getDefault().post(event);
    }

    public void setWatch_status_index_mapping(){
        watch_status_index_mapping.add(0,"currently-watching");
        watch_status_index_mapping.add(1, "completed");
        watch_status_index_mapping.add(2, "plan-to-watch" );
        watch_status_index_mapping.add(3, "dropped");
    }
}
