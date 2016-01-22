package hummingbird.android.mobile_app.presenters;

import android.view.View;

import de.greenrobot.event.EventBus;

/**
 * Created by CzeWen on 2016-01-15.
 */
public class Presenter {
    View view;
    public Presenter(){
    }

    public Presenter(View view){
        this.view = view;
    }

    public void bindView(View view){
        this.view = view;
    }

    public void unbindView(){
        this.view = null;
    }

    public void onPause(){
        EventBus.getDefault().unregister(this);
    }

    public void onResume(){
        EventBus.getDefault().register(this);
    }

    public void onDestroy(){
        EventBus.getDefault().unregister(this);
    }
}
