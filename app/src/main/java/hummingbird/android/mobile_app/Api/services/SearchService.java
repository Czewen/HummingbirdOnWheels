package hummingbird.android.mobile_app.Api.services;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.HummingbirdApiService;
import hummingbird.android.mobile_app.events.GetAnimeSuccessEvent;
import hummingbird.android.mobile_app.events.NetworkFailureEvent;
import hummingbird.android.mobile_app.events.ResponseFailureEvent;
import hummingbird.android.mobile_app.events.SearchAnimeEvent;
import hummingbird.android.mobile_app.events.SearchAnimeSuccessEvent;
import hummingbird.android.mobile_app.models.Anime;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by CzeWen on 2016-05-17.
 */
public class SearchService extends Service {

    Retrofit api;
    HummingbirdApiService api_v1_service;

    public SearchService(){
        api = getHummingbirdV1RetrofitInstance();
        api_v1_service = api.create(HummingbirdApiService.class);
        EventBus.getDefault().register(this);
    }


    public void onEventAsync(SearchAnimeEvent event){
        String query = event.getQuery_String();
        api_v1_service.searchAnimeByTitle(query).enqueue(new Callback<ArrayList<Anime>>() {
            @Override
            public void onResponse(Response<ArrayList<Anime>> response, Retrofit retrofit) {
                if(response.code() == 200){
                    EventBus.getDefault().post(new SearchAnimeSuccessEvent(response.body()));
                }
                else{
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
