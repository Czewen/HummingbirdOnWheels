package hummingbird.android.mobile_app.events;

/**
 * Created by CzeWen on 2016-01-18.
 */
public class GetAnimeEvent {

    private int anime_id;

    public GetAnimeEvent(int anime_id){
        this.anime_id = anime_id;
    }
    public GetAnimeEvent(String anime_id){
        this.anime_id = Integer.parseInt(anime_id);
    }

    public int getAnimeId(){
        return anime_id;
    }
}
