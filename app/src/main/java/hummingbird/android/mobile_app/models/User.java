package hummingbird.android.mobile_app.models;

import java.util.ArrayList;
/**
 * Created by CzeWen on 2015-12-20.
 */
public class User {

    private String name;
    private String waifu;
    private String waifu_or_husbando;
    private String waifu_slug;
    private int waifu_char_id;
    private String location;
    private String website;
    private String avatar;
    private String cover_image;
    private String about;
    private String bio;
    private int karma;
    private int life_spent_on_anime;
    private boolean show_adult_content;
    private String last_library_update;
    private boolean online;
    ArrayList<FavoriteObject> favorites;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setWaifu(String waifu){
        this.waifu = waifu;
    }

    public String getWaifu(){
        return waifu;
    }

    public void setWaifu_or_husbando(String waifu_husbando){
        this.waifu_or_husbando = waifu_husbando;
    }

    public String getWaifu_or_husbando(){
        return waifu_or_husbando;
    }

    public void setWaifu_slug(String waifu_slug){
        this.waifu_slug = waifu_slug;
    }

    public String getWaifu_slug(){
        return waifu_slug;
    }

    public void setWaifu_char_id(int waifu_char_id){
        this.waifu_char_id = waifu_char_id;
    }

    public boolean isOnline(){
        return online;
    }

    public String getBio(){
        return bio;
    }

    public ArrayList<FavoriteObject> getFavAnime(){
        return favorites;
    }

    public String getAvatar(){
        return avatar;
    }

    public String getCover_image(){
        return cover_image;
    }

}
