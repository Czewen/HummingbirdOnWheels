package hummingbird.android.mobile_app.Api.services;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.HummingbirdApiService;
import hummingbird.android.mobile_app.events.GetLibraryEvent;
import hummingbird.android.mobile_app.events.GetLibrarySuccessEvent;
import hummingbird.android.mobile_app.events.NetworkFailureEvent;
import hummingbird.android.mobile_app.events.ResponseFailureEvent;
import hummingbird.android.mobile_app.events.UpdateLibraryEvent;
import hummingbird.android.mobile_app.events.UpdateLibrarySuccessEvent;
import hummingbird.android.mobile_app.models.LibraryEntry;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by CzeWen on 2016-01-21.
 */
public class LibraryService extends Service{

    Retrofit api;
    HummingbirdApiService api_v1_service;

    public LibraryService(){
        api = getHummingbirdV1RetrofitInstance();
        api_v1_service = api.create(HummingbirdApiService.class);
        EventBus.getDefault().register(this);
    }


    public void onEventAsync(GetLibraryEvent library_event){
        String username = library_event.getUsername();
        Call<ArrayList<LibraryEntry>> get_libraries_callback;
        if(username.contentEquals("me")){
            String auth_token = library_event.getAuth_token();
            get_libraries_callback = api_v1_service.getUserLibrary(username, auth_token);
        }
        else{
            get_libraries_callback = api_v1_service.getUserLibrary(username);
        }
        get_libraries_callback.enqueue(new Callback<ArrayList<LibraryEntry>>() {
            @Override
            public void onResponse(Response<ArrayList<LibraryEntry>> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new GetLibrarySuccessEvent(response.body()));
                    return;
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
                EventBus.getDefault().post(new NetworkFailureEvent(t.getMessage()));
                Log.d("Get_profile", t.getStackTrace().toString(), t);
            }
        });
    }

    public void onEventAsync(UpdateLibraryEvent event){
        final String auth_token = event.getAuth_Token();
        final String update_type = event.update_type;
        Map<String, String> params  = new HashMap<>();
        params.put("auth_token", auth_token);
        params.put(update_type, event.value);
        api_v1_service.updateLibraryEntry(event.getAnime_id(), params).enqueue(new Callback<LibraryEntry>() {
            @Override
            public void onResponse(Response<LibraryEntry> response, Retrofit retrofit) {
                if(response.code()==201 || response.code() == 200)
                    EventBus.getDefault().post(new UpdateLibrarySuccessEvent(update_type, response.body()));
                else {
                    int http_error_code = response.code();
                    String failure_reason = response.message();
                    EventBus.getDefault().post(new ResponseFailureEvent(http_error_code, failure_reason));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                EventBus.getDefault().post(new NetworkFailureEvent(t.getMessage()));
            }
        });
    }


}
