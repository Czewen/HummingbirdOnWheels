package hummingbird.android.mobile_app.presenters;

import android.view.View;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.services.Service;

/**
 * Created by CzeWen on 2016-01-15.
 */
public class Presenter {
    View view;
    Service service;
    ArrayList<Service> services;

    public Presenter(){
        services = new ArrayList<>();
    }

    public void unbindView(){
        for(Service service : services)
            services.remove(service);
    }

    public void onPause(){
        EventBus.getDefault().unregister(this);
        for(Service service : services)
            service.stopListening();
//        service.stopListening();
    }

    public void onStop(){
        EventBus.getDefault().unregister(this);
        for(Service service : services)
            service.stopListening();
//        EventBus.getDefault().unregister(service);
    }

    public void onResume(){
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        for(Service service : services)
            service.startListening();
//        if(!service.isListening()){
//            EventBus.getDefault().register(service);
//        }
    }

    public void onDestroy(){
        EventBus.getDefault().unregister(this);
        for(Service service : services)
            service.stopListening();
//        EventBus.getDefault().unregister(service);
    }

    public void bindService(Service service){
        services.add(service);
//        this.service = service;
    }

}
