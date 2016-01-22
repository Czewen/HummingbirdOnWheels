package hummingbird.android.mobile_app.Api;
import android.os.AsyncTask;
import java.io.IOException;
import hummingbird.android.mobile_app.views.LoginView;
import retrofit.Retrofit;
import retrofit.Response;
import retrofit.GsonConverterFactory;
import java.util.HashMap;
import java.util.Map;
import hummingbird.android.mobile_app.Api.services.Service;
/**
 * Created by CzeWen on 2015-12-01.
 */
public class ApiCaller extends Service {

    Retrofit api_v1;
    HummingbirdApiService api_v1_service;

    public ApiCaller(){
        api_v1 = new Retrofit.Builder().baseUrl("https://hummingbird.me/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api_v1_service = api_v1.create(HummingbirdApiService.class);
    }

    public void authenticate(final String username_or_email, final String password, final LoginView login_view) {
        class TaskParams{

            Map<String, String> api_params = new HashMap<String, String>();
            TaskParams(String username_email, String password){
                if(username_email.contains("@"))
                    api_params.put("email", username_email);
                else
                    api_params.put("username", username_email);
                api_params.put("password", password);
            }
        }
        class authenticateTask extends AsyncTask<TaskParams, Void, Response<String>>{
            IOException io_occured;
            protected Response<String> doInBackground(TaskParams... params){
                try{
                    Response<String> result;
                    result = api_v1_service.authenticate(params[0].api_params).execute();
                    return result;
                }
                catch(IOException io_exception){
                    io_occured = io_exception;
                    return null;
                }
            }
            protected void onPostExecute(Response<String> result){
                if(result == null) {
                    if(!(io_occured==null)){
                        login_view.displayLoginFailed(io_occured.getMessage());
                        return;
                    }
                    login_view.displayLoginFailed("API call returned null response");
                    return;
                }
                if(result.code() == 201){
                    login_view.loginSuccess("me", result.body());
                }
                else{
                    login_view.displayLoginFailed(result.code()+": "+ result.errorBody());
                }
            }
        }
       new authenticateTask().execute(new TaskParams(username_or_email, password));
    }
    public void searchUser(String username){

    }

}
