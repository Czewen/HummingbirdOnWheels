package hummingbird.android.mobile_app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CzeWen on 2016-01-07.
 */
public class LibraryRating implements Parcelable {

    public String type;
    public float value;

    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeString(type);
        out.writeFloat(value);
    }

    public LibraryRating(){}

    private LibraryRating(Parcel in){
        type = in.readString();
        value = in.readFloat();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public static LibraryRating.Creator<LibraryRating> CREATOR = new LibraryRating.Creator<LibraryRating>(){
        @Override
        public LibraryRating createFromParcel(Parcel in){
            return new LibraryRating(in);
        }
        @Override
        public LibraryRating[] newArray(int size){
            return new LibraryRating[size];
        }
    };
}
