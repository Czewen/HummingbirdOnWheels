package hummingbird.android.mobile_app.presenters;

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

    public AnimePresenter(AnimeView view){
        this.view = view;
        anime_service = new AnimeService();
        library_service = new LibraryService();
        EventBus.getDefault().register(this);
        super.bindService(anime_service);
        super.bindService(library_service);
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
        if(is_library_entry)
            view.setEpisodesWatched(library_entry.episodes_watched);
    }

    public void onEvent(UpdateLibrarySuccessEvent event){
        switch(event.getUpdate_type()){
            case "episodes_watched":
                break;
        }
    }

    public void setLibraryEntry(LibraryEntry entry){
        is_library_entry = true;
        library_entry = entry;
    }


}
