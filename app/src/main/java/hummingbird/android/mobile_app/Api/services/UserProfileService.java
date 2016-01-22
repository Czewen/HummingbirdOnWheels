package hummingbird.android.mobile_app.Api.services;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.HummingbirdApiService;
import hummingbird.android.mobile_app.events.GetLibraryEvent;
import hummingbird.android.mobile_app.events.GetLibrarySuccessEvent;
import hummingbird.android.mobile_app.events.GetProfileSuccessEvent;
import hummingbird.android.mobile_app.events.GetUserProfileEvent;
import hummingbird.android.mobile_app.events.NetworkFailureEvent;
import hummingbird.android.mobile_app.events.ResponseFailureEvent;
import hummingbird.android.mobile_app.models.LibraryEntry;
import hummingbird.android.mobile_app.models.User;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
/**
 * Created by CzeWen on 2016-01-02.
 */
public class UserProfileService extends Service {
    Retrofit api_v1;
    HummingbirdApiService api_v1_service;
    private final String api_v1_base = "https://hummingbird.me/api/v1/";


    public UserProfileService(){
        api_v1 = new Retrofit.Builder().baseUrl(api_v1_base)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api_v1_service = api_v1.create(HummingbirdApiService.class);
        EventBus.getDefault().register(this);
    }

    public void onEvent(GetUserProfileEvent event){
        String user_profile_name = event.getUser_profile_name();
        Response<User> response;
        Call<User> get_profile_call;
        if(user_profile_name.contentEquals("me") && event.getAuth_token()!=null){
            get_profile_call = api_v1_service.getUserInformation(user_profile_name, event.getAuth_token());
        }
        else{
            get_profile_call = api_v1_service.getUserInformation(user_profile_name);
        }
        //api_v1_service.getUserInformation(api_v1_base+"/users/"+user_profile_name).enqueue(new Callback<User>() {
        get_profile_call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    EventBus.getDefault().post(new GetProfileSuccessEvent(response.body()));
                } else {
                    int http_error_code = response.code();
                    String failure_reason = response.message();
                    EventBus.getDefault().post(new ResponseFailureEvent(http_error_code, failure_reason));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t instanceof IOException) {
                    EventBus.getDefault().post(new NetworkFailureEvent(t.getMessage()));
                }
                Log.d("Get_profile", t.getStackTrace().toString(), t);
            }
        });
    }


    public void onEvent(GetLibraryEvent event) {
        String username_profile_name = event.getUsername();
        Call<ArrayList<LibraryEntry>> get_library_call;
        if(username_profile_name.contentEquals("me")&& event.getAuth_token()!=null ){
            get_library_call = api_v1_service.getUserLibrary(username_profile_name, event.getAuth_token());
        }
        else{
            get_library_call = api_v1_service.getUserLibrary(username_profile_name);
        }
        get_library_call.enqueue(new Callback<ArrayList<LibraryEntry>>() {
            @Override
            public void onResponse(Response<ArrayList<LibraryEntry>> response, Retrofit retrofit) {
                if( response.code() == 200) {
                    EventBus.getDefault().post(new GetLibrarySuccessEvent(response.body()));
                }
                else {
                    int http_error_code = response.code();
                    String failure_reason = response.message();
                    EventBus.getDefault().post(new ResponseFailureEvent(http_error_code, failure_reason));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t instanceof IOException) {
                    EventBus.getDefault().post(new NetworkFailureEvent(t.getMessage()));
                }
                EventBus.getDefault().post(new NetworkFailureEvent(t.getMessage()));
                Log.d("Get_profile", t.getStackTrace().toString(), t);
            }
        });
    }

}
