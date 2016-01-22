package hummingbird.android.mobile_app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CzeWen on 2016-01-07.
 */
public class LibraryEntry implements Parcelable{
    public int id;
    public int episodes_watched;
    public String last_watched;
    public String updated_at;
    public int rewatched_times;
    public String notes;
    public String notes_present;
    public String status;
    public boolean rewatching;
    public Anime anime;
    public LibraryRating rating;

    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeInt(id);
        out.writeInt(episodes_watched);
        out.writeString(last_watched);
        out.writeString(updated_at);
        out.writeInt(rewatched_times);
        out.writeString(notes);
        out.writeString(notes_present);
        out.writeString(status);
        out.writeValue(rewatching);
        out.writeParcelable(anime, flags);
        out.writeParcelable(rating, flags);
    }

    public LibraryEntry(){}


    private LibraryEntry(Parcel in){
        id = in.readInt();
        episodes_watched = in.readInt();
        last_watched = in.readString();
        updated_at = in.readString();
        rewatched_times = in.readInt();
        notes = in.readString();
        notes_present = in.readString();
        status = in.readString();
        rewatching = (Boolean) in.readValue( null );
        anime = in.readParcelable(Anime.class.getClassLoader());
        rating = in.readParcelable(LibraryRating.class.getClassLoader());
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public static LibraryEntry.Creator<LibraryEntry> CREATOR = new LibraryEntry.Creator<LibraryEntry>(){
        @Override
        public LibraryEntry createFromParcel(Parcel in){
            return new LibraryEntry(in);
        }

        @Override
        public LibraryEntry[] newArray(int size){
            return new LibraryEntry[size];
        }
    };

}
