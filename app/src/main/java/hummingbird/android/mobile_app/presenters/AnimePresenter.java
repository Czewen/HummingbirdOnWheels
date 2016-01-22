package hummingbird.android.mobile_app.presenters;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.services.AnimeService;
import hummingbird.android.mobile_app.events.GetAnimeEvent;
import hummingbird.android.mobile_app.events.GetAnimeSuccessEvent;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.views.activities.AnimeView;

/**
 * Created by CzeWen on 2016-01-18.
 */
public class AnimePresenter {
    AnimeView view;
    AnimeService anime_service;
    Anime anime;

    public AnimePresenter(AnimeView view){
        this.view = view;
        anime_service = new AnimeService();
        EventBus.getDefault().register(this);
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
    }


}
