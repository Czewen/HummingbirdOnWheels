package hummingbird.android.mobile_app.events;

/**
 * Created by CzeWen on 2016-04-26.
 */
public class UpdateLibraryEvent {

    private String auth_token;
    private int anime_id;
    public String update_type;
    public String value;

    public UpdateLibraryEvent(String auth_token, int anime_id){
        this.auth_token = auth_token;
        this.anime_id = anime_id;
    }

    public UpdateLibraryEvent(String auth_token, String anime_id){
        this.auth_token = auth_token;
        this.anime_id = Integer.parseInt(anime_id);
    }
    public void setUpdate_type(String update_type){
        this.update_type = update_type;
    }
    public String getAuth_Token(){
        return auth_token;
    }

    public int getAnime_id(){
        return anime_id;
    }


}
