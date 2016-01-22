package hummingbird.android.mobile_app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CzeWen on 2016-01-07.
 */
public class Genre implements Parcelable{
    public String name;

    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeString(name);
    }

    public Genre(){}

    private Genre(Parcel in){
        name = in.readString();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public static final Parcelable.Creator<Genre> CREATOR =  new Parcelable.Creator<Genre>(){
        @Override
        public Genre createFromParcel(Parcel in){
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size){
            return new Genre[size];
        }
    };


}
