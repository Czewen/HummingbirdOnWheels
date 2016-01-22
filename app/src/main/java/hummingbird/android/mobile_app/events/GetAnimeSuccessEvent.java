package hummingbird.android.mobile_app.events;

import hummingbird.android.mobile_app.models.Anime;

/**
 * Created by CzeWen on 2016-01-18.
 */
public class GetAnimeSuccessEvent {

    Anime anime_obj;

    public GetAnimeSuccessEvent(Anime anime_obj){
        this.anime_obj = anime_obj;
    }

    public Anime getAnimeObject(){
        return anime_obj;
    }
}
