package hummingbird.android.mobile_app.Api.services;

import de.greenrobot.event.EventBus;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by CzeWen on 2015-12-26.
 */
public class Service {

    public static String v1_end_point = "https://hummingbird.me/api/v1/";
    public static String v3_end_point = "https://hummingbird.me/api/v3/";

    public void stopListening(){
        EventBus.getDefault().unregister(this);
    }

    public void startListening(){
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    public boolean isListening(){
        return EventBus.getDefault().isRegistered(this);
    }

    public static Retrofit getHummingbirdV1RetrofitInstance(){
        Retrofit api_v1 = new Retrofit.Builder()
                .baseUrl(v1_end_point)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return api_v1;
    }

    public static Retrofit getHummingbirdV1RetrofitInstance(GsonConverterFactory converter){
        Retrofit api_v1 = new Retrofit.Builder()
                .baseUrl(v1_end_point)
                .addConverterFactory(converter)
                .build();
        return api_v1;
    }

}
