package hummingbird.android.mobile_app.presenters;

import android.view.View;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.services.Service;

/**
 * Created by CzeWen on 2016-01-15.
 */
public class Presenter {
    View view;
    Service service;

    public Presenter(){
    }

    public void unbindView(){
        this.view = null;
    }

    public void onPause(){
        EventBus.getDefault().unregister(this);
        service.stopListening();
    }

    public void onStop(){
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().unregister(service);
    }

    public void onResume(){
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        if(!service.isListening()){
            EventBus.getDefault().register(service);
        }
    }

    public void onDestroy(){
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().unregister(service);
    }

    public void bindService(Service service){
        this.service = service;
    }

}
