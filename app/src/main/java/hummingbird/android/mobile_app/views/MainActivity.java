package hummingbird.android.mobile_app.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.*;
import android.widget.EditText;
import hummingbird.android.mobile_app.events.LoginEvent;
import hummingbird.android.mobile_app.events.LoginSuccessEvent;
import hummingbird.android.mobile_app.presenters.LoginPresenter;
import hummingbird.android.mobile_app.views.LoginView;
import hummingbird.android.mobile_app.R;
import android.os.StrictMode;
import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.views.activities.ProfileActivity;
import hummingbird.android.mobile_app.views.activities.TestFragment;

public class MainActivity extends AppCompatActivity implements LoginView {
    public final static String EXTRA_MESSAGE = "hummingbird.android.mobile_app.MESSAGE";
    public LoginPresenter login_presenter;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        login_presenter = new LoginPresenter(this);
        //EventBus.getDefault().register(this);
    }


    public void sendMessage(View view){
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        Intent intent = new Intent(this, TestFragment.class);
        EditText textbox = (EditText) findViewById(R.id.edit_message);
        String message =  textbox.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void login(View view){
        EditText username_input = (EditText) findViewById(R.id.login_username_textbox);
        username = username_input.getText().toString();
        EditText password_input = (EditText) findViewById(R.id.login_password_textbox);
        String password = password_input.getText().toString();
        //EventBus.getDefault().post(new LoginEvent(username, password));
        //login_presenter.authenticate_login(username, password);
        String message = "";
        if(login_presenter.isServiceListening()){
            message = "Listening";
        }
        else{
            message = "Not listening";
        }
        EditText auth_token_textbox = (EditText) findViewById(R.id.auth_token);
        auth_token_textbox.setText(message);
        login_presenter.login(username, password);
        //String result = login_presenter.authenticate_login(username, password);
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, result+" token");
        //startActivity(intent);
    }


    public void loginSuccess(String username, String auth_token){
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(EXTRA_MESSAGE, auth_token);
        intent.putExtra("username", username);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("Hummingbird_on_wheels", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("auth_token", auth_token);
        editor.commit();
        startActivity(intent);
    }

    public void displayLoginFailed(String error){
        EditText auth_token_textbox = (EditText) findViewById(R.id.auth_token);
        auth_token_textbox.setText(error);
    }

    public void setAuthToken(String auth_token){
        EditText auth_token_textbox = (EditText) findViewById(R.id.auth_token);
        auth_token_textbox.setText(auth_token);
    }

    public void onPause(){
        super.onPause();
        login_presenter.onPause();
    }

    public void editTest(String test){
        EditText edit_test = (EditText) findViewById(R.id.edit_message);
        edit_test.setText(test);
    }

    public void onResume(){
        super.onResume();
        login_presenter.onResume();
    }

    public void onDestroy(){
        super.onDestroy();
        login_presenter.onDestroy();
    }



}
