package hummingbird.android.mobile_app.Api.services;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import hummingbird.android.mobile_app.events.LoginFailedEvent;
import hummingbird.android.mobile_app.events.LoginSuccessEvent;
import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.HummingbirdApiService;
import hummingbird.android.mobile_app.views.LoginView;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import hummingbird.android.mobile_app.events.LoginEvent;
/**
 * Created by CzeWen on 2015-12-21.
 */
public class LoginService extends Service {

    Retrofit api_v1;
    HummingbirdApiService api_v1_service;
    LoginView login_view;

    public LoginService(LoginView login_view){
        api_v1 = new Retrofit.Builder().baseUrl("https://hummingbird.me/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api_v1_service = api_v1.create(HummingbirdApiService.class);
        EventBus.getDefault().register(this);
        this.login_view = login_view;
    }

    public void onEvent(LoginEvent event) {
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
        class authenticateTask extends AsyncTask<TaskParams, Void, Response<String>> {
            private String username;
            protected Response<String> doInBackground(TaskParams... params){
                try{
                    Response<String> result;
                    if(params[0].api_params.get("username")==null){
                        username = params[0].api_params.get("email");
                    }
                    else{
                        username = params[0].api_params.get("username");
                    }
                    result = api_v1_service.authenticate(params[0].api_params).execute();
                    return result;
                }
                catch(IOException io_exception){
                    EventBus.getDefault().post(new LoginFailedEvent(io_exception.getMessage()));
                    return null;
                }
            }
            protected void onPostExecute(Response<String> result){
                String error = "Sorry, an error occurred. Please try again.";
                if(result!=null){
                    if(result.code() == 201){
                        //login_view.loginSuccess(result.body());
                        EventBus.getDefault().post(new LoginSuccessEvent(username, result.body()));
                        return;
                    }
                    else if(result.code() == 401){
                        error = "Invalid password or username. Please try again.";
                    }
                }
                EventBus.getDefault().post(new LoginFailedEvent(error));
            }
        }
        new authenticateTask().execute(new TaskParams(event.getLoginName(), event.getPassword()));
    }



}
