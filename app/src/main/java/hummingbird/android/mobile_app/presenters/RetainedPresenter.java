package hummingbird.android.mobile_app.presenters;


import android.app.Fragment;
import android.os.Bundle;


/**
 * Created by CzeWen on 2016-01-15.
 */
public class RetainedPresenter<T> extends Fragment{

    private T dataObject;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setData(T dataObject){
        this.dataObject = dataObject;
    }

    public T getData(){
        return dataObject;
    }

}
