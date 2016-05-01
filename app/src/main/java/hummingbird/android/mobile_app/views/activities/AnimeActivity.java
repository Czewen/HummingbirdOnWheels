package hummingbird.android.mobile_app.views.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.models.LibraryEntry;
import hummingbird.android.mobile_app.presenters.AnimePresenter;
import hummingbird.android.mobile_app.presenters.ProfilePresenter;
import hummingbird.android.mobile_app.presenters.RetainedPresenter;

/**
 * Created by CzeWen on 2016-01-18.
 */
public class AnimeActivity extends AppCompatActivity implements AnimeView{

    int id;
    Anime anime_obj;
    AnimePresenter anime_presenter;
    RetainedPresenter<AnimePresenter> retained_presenter;

    ImageView cover_photo;
    TextView title_view;
    TextView episodes_watched;
    TextView no_episodes;
    Spinner watch_status_spinner;

    private boolean userIsInteracting = false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);
        Bundle extras = getIntent().getExtras();

        Intent test = getIntent();
        id = extras.getInt("id");

        FragmentManager fm = getFragmentManager();
        retained_presenter = (RetainedPresenter<AnimePresenter>)fm.findFragmentByTag("data");
        if(retained_presenter == null){
            retained_presenter = new RetainedPresenter<AnimePresenter>();
            anime_presenter = new AnimePresenter(this);
            if(extras.getBoolean("isLibraryEntry"))
                anime_presenter.setLibraryEntry((LibraryEntry)extras.get("LibraryEntry"));
            fm.beginTransaction().add(retained_presenter, "data");
            retained_presenter.setData(anime_presenter);
        }
        else{
            anime_presenter = retained_presenter.getData();
        }
        anime_presenter.fetchAnime(id);
        watch_status_spinner = (Spinner) findViewById(R.id.watch_status);
        watch_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(userIsInteracting){
                    anime_presenter.updateWatchStatus(getAuthToken(), position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setCoverPhoto(String url){
        if(cover_photo ==  null) {
            cover_photo = (ImageView) findViewById(R.id.anime_cover_photo);
        }
        Picasso.with(this)
                .load(url)
                .into(cover_photo);
    }

    public void setTitle(String title){
        if(title_view==null){
            title_view = (TextView) findViewById(R.id.anime_title);
        }
        title_view.setText(title);
    }

    public void setEpisodeCount(int episode_count){
        if(no_episodes == null){
            no_episodes = (TextView) findViewById(R.id.no_episodes);
        }
        no_episodes.setText("/" + episode_count);
        //String number_of_episodes = "/"+episode_count;
    }

    public void increaseWatchedEpisodes(View view){
        anime_presenter.updateEpisodesWatched(getAuthToken(), Integer.parseInt(episodes_watched.getText().toString()));
    }

    public void setEpisodesWatched(int episodes_watched_value){
        if(episodes_watched == null)
            episodes_watched = (TextView) findViewById(R.id.episodes_watched);
        episodes_watched.setText(Integer.toString(episodes_watched_value));
    }

    public void setWatchStatus(int watch_status_index){
        userIsInteracting = false;
        watch_status_spinner.setSelection(watch_status_index);
    }

    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        userIsInteracting = true;
    }

    private String getAuthToken(){
        SharedPreferences prefs = getSharedPreferences("Hummingbird_on_wheels", Context.MODE_PRIVATE);
        String auth_token = prefs.getString("auth_token", "token_missing");
        return auth_token;
    }


}
