package hummingbird.android.mobile_app.Api;
import hummingbird.android.mobile_app.models.*;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Field;
import retrofit.Call;

import java.util.ArrayList;
import java.util.Map;

import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.http.Url;


/**
 * Created by CzeWen on 2015-12-01.
 */
public interface HummingbirdApiService {
    @FormUrlEncoded
    @POST("users/authenticate")
    Call<String> authenticateWithUsername(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("users/authenticate")
    Call<String> authenticateWithEmail(@Field("email") String username, @Field("password") String password);

    @POST("users/authenticate")
    Call<String> authenticate(@QueryMap Map<String, String> options);

    @GET("users/{username}")
    Call<User> getUserInformation(@Path("username") String username);

    @GET("users/{username}")
    Call<User> getUserInformation(@Path("username") String username, @Query("auth_token") String auth_token);

    @GET("users/{username}/library")
    Call<ArrayList<LibraryEntry>> getUserLibrary(@Path("username") String username);

    @GET("users/{username}/library")
    Call<ArrayList<LibraryEntry>> getUserLibrary(@Path("username") String username, @Query("auth_token") String auth_token);

    @GET("anime/{id}")
    Call<Anime> getAnime(@Path("id") int id);

    @POST("libraries/{id}")
    Call<LibraryEntry> updateLibraryEntry(@Path("id") int library_entry_id, @QueryMap Map<String, String> params);

    @GET("search/anime")
    Call<ArrayList<Anime>> searchAnimeByTitle(@Query("query") String query_string);

}
