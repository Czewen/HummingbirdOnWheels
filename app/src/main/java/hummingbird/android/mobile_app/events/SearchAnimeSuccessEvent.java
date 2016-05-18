package hummingbird.android.mobile_app.events;

import java.util.ArrayList;

import hummingbird.android.mobile_app.models.Anime;

/**
 * Created by CzeWen on 2016-05-17.
 */
public class SearchAnimeSuccessEvent {

    ArrayList<Anime> results;

    public SearchAnimeSuccessEvent(ArrayList<Anime> results){
        this.results = results;
    }

    public ArrayList<Anime> getResults(){
        return results;
    }
}
