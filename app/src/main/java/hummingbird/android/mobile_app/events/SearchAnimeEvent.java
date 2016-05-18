package hummingbird.android.mobile_app.events;

/**
 * Created by CzeWen on 2016-05-17.
 */
public class SearchAnimeEvent {

    String query_string;

    public SearchAnimeEvent(String query_string){
        this.query_string = query_string;
    }

    public String getQuery_String(){
        return query_string;
    }
}
