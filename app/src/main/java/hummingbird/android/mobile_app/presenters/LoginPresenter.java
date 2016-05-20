package hummingbird.android.mobile_app.presenters;

/**
 * Created by CzeWen on 2015-11-29.
 */
import android.app.Activity;
import android.util.Log;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.services.LibraryService;
import hummingbird.android.mobile_app.Api.services.LoginService;
import hummingbird.android.mobile_app.Exceptions.InvalidLoginCredentialException;
import hummingbird.android.mobile_app.database.DBStore;
import hummingbird.android.mobile_app.events.LoginEvent;
import hummingbird.android.mobile_app.events.LoginFailedEvent;
import hummingbird.android.mobile_app.events.LoginSuccessEvent;
import hummingbird.android.mobile_app.views.*;
import hummingbird.android.mobile_app.Api.ApiCaller;

public class LoginPresenter {
    LoginView login_page;
    ApiCaller api_caller;
    LoginService login_service;

    public LoginPresenter(LoginView view){
        login_page = view;
        api_caller = new ApiCaller();
        login_service = new LoginService(view);
        EventBus.getDefault().register(this);
    }

    public void authenticate_login(String login_name, String password){
        api_caller.authenticate(login_name, password, login_page);
    }

    public void onResume(){
        if(EventBus.getDefault().isRegistered(this) == false)
            EventBus.getDefault().register(this);
        if(EventBus.getDefault().isRegistered(login_service) == false)
            login_service.startListening();
    }

    public void onPause(){
        EventBus.getDefault().unregister(this);
        //api_caller.stopListening();
        login_service.stopListening();
    }

    public void onDestroy(){
        EventBus.getDefault().unregister(this);
        //api_caller.stopListening();
        login_service.stopListening();
    }

    public void login(String login_name, String password){
        EventBus.getDefault().post(new LoginEvent(login_name, password));
    }

    public void onEvent(LoginSuccessEvent login_success){
        String oauth_token = login_success.getOauth_token();
        String username = login_success.getUsername();
        DBStore db = DBStore.getInstance((Activity) login_page);
//        try {
            if (!db.userExistsInDB(username)) {
                db.insertUser(username);
                LibraryService.initializeDBWithLibraryEntries(db, username);
            }
//        }
//        catch(Exception e){
//            //STUB will handle later
//        }
        login_page.loginSuccess(username, oauth_token);
    }

    public void onEvent(LoginFailedEvent login_failed){
        Log.e("login_failed", login_failed.getError());
        login_page.setAuthToken(login_failed.getError());
    }

    public boolean isServiceListening(){
        return EventBus.getDefault().isRegistered(login_service);
    }




}
