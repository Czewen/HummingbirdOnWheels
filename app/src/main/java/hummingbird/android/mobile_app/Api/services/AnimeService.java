package hummingbird.android.mobile_app.Api.services;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.HummingbirdApiService;
import hummingbird.android.mobile_app.events.GetAnimeEvent;
import hummingbird.android.mobile_app.events.GetAnimeSuccessEvent;
import hummingbird.android.mobile_app.events.NetworkFailureEvent;
import hummingbird.android.mobile_app.events.ResponseFailureEvent;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.views.LoginView;
import hummingbird.android.mobile_app.views.activities.AnimeView;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by CzeWen on 2016-01-18.
 */
public class AnimeService extends Service {

    Retrofit api_v1;
    HummingbirdApiService api_v1_service;

    public AnimeService(){
        api_v1 = super.getHummingbirdV1RetrofitInstance();
        api_v1_service = api_v1.create(HummingbirdApiService.class);
        EventBus.getDefault().register(this);
        //api_v1 = new Retrofit.Builder()
         //       .baseUrl(v1_end_point)
          //      .addConverterFactory(GsonConverterFactory.create())
           //     .build();
        //api_v1_service = api_v1.create(HummingbirdApiService.class);
    }

    public void onEvent(GetAnimeEvent event){
        int anime_id = event.getAnimeId();
        Response<Anime> response;
        api_v1_service.getAnime(anime_id).enqueue(new Callback<Anime>() {
            @Override
            public void onResponse(Response<Anime> response, Retrofit retrofit) {
                if(response.code() == 200) {
                    EventBus.getDefault().post(new GetAnimeSuccessEvent(response.body()));
                    return;
                }

                int http_error_code = response.code();
                String failure_reason = response.message();
                EventBus.getDefault().post(new ResponseFailureEvent(http_error_code, failure_reason));
            }

            @Override
            public void onFailure(Throwable t) {
                EventBus.getDefault().post(new NetworkFailureEvent(t.getMessage()));
            }
        });
    }


}
