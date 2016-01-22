package hummingbird.android.mobile_app.models;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
/**
 * Created by CzeWen on 2016-01-07.
 */
public class Anime implements Parcelable {

    public int id;
    public int mal_id;
    public String status;
    public String url;
    public String title;
    public String alternate_title;
    public int episode_count;
    public int episode_length;
    public String cover_image;
    public String sypnopsis;
    public String show_type;
    public String started_airing;
    public String finished_airing;
    public float community_rating;
    public String age_rating;
    public ArrayList<Genre> genres;

    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeInt(id);
        out.writeInt(mal_id);
        out.writeString(status);
        out.writeString(url);
        out.writeString(title);
        out.writeString(alternate_title);
        out.writeInt(episode_count);
        out.writeInt(episode_length);
        out.writeString(cover_image);
        out.writeString(sypnopsis);
        out.writeString(show_type);
        out.writeString(started_airing);
        out.writeString(finished_airing);
        out.writeFloat(community_rating);
        out.writeString(age_rating);
        out.writeTypedList(genres);
    }

    public Anime(){}

    private Anime(Parcel in){
        id = in.readInt();
        mal_id = in.readInt();
        status = in.readString();
        url = in.readString();
        title = in.readString();
        alternate_title = in.readString();
        episode_count = in.readInt();
        episode_length = in.readInt();
        cover_image = in.readString();
        sypnopsis = in.readString();
        show_type = in.readString();
        started_airing = in.readString();
        finished_airing = in.readString();
        community_rating = in.readFloat();
        age_rating = in.readString();
        in.readTypedList(genres, Genre.CREATOR);
    }
     @Override
    public int describeContents(){
        return 0;
    }

    public static Anime.Creator<Anime> CREATOR = new Anime.Creator<Anime>(){
        @Override
        public Anime createFromParcel(Parcel in){
            return new Anime(in);
        }

        @Override
        public Anime[] newArray(int size){
            return new Anime[size];
        }
    };
}
