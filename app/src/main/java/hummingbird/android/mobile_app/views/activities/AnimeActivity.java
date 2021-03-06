package hummingbird.android.mobile_app.views.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.models.LibraryEntry;
import hummingbird.android.mobile_app.presenters.AnimePresenter;
import hummingbird.android.mobile_app.presenters.RetainedPresenter;

/**
 * Created by CzeWen on 2016-01-18.
 */
public class AnimeActivity extends Activity implements AnimeView{

    int id;
    Anime anime_obj;
    AnimePresenter anime_presenter;
    RetainedPresenter<AnimePresenter> retained_presenter;

    ImageView cover_photo;
    TextView title_view;
    TextView episodes_watched;
    TextView no_episodes;
    TextView episode_count_in_description;
    Spinner watch_status_spinner;
    TextView show_type;
    TextView show_status;

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
            anime_presenter.setLibrary_entry_position(extras.getInt("LibraryEntryPosition"));
        }
        else{
            anime_presenter = retained_presenter.getData();
        }
        anime_presenter.fetchAnime(id);

        //setup watch status spinner
        String[] watch_status_spinner_values = getResources().getStringArray(R.array.watch_status_values);
        ArrayAdapter<String> watch_status_adapter = new ArrayAdapter<String>(this, R.layout.watch_status_layout, watch_status_spinner_values);
        watch_status_spinner = (Spinner) findViewById(R.id.watch_status);
        watch_status_spinner.setAdapter(watch_status_adapter);
        watch_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (userIsInteracting) {
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
        if(episode_count_in_description == null)
            episode_count_in_description = (TextView) findViewById(R.id.anime_episode_count_description);
        episode_count_in_description.setText(Integer.toString(episode_count));

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

    public void setShowType(String type){
        if(show_type == null)
            show_type = (TextView) findViewById(R.id.anime_show_type);
        show_type.setText(type);
    }

    public void setShowStatus(String status){
        if(show_status == null)
            show_status = (TextView) findViewById(R.id.anime_show_status);
        show_status.setText(status);
    }

    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        Intent returnIntent = new Intent();
        if(anime_presenter.hasUpdates()){
            bundle.putSerializable("LibraryEntryUpdates", anime_presenter.getSuccessfulUpdates());
            bundle.putInt("LibraryEntryPosition", anime_presenter.getLibrary_entry_position());
            setResult(RESULT_OK, returnIntent);
        }
        else{
            setResult(RESULT_CANCELED, returnIntent);
        }
        returnIntent.putExtras(bundle);
        finish();
        super.onBackPressed();
    }

    private String getAuthToken(){
        SharedPreferences prefs = getSharedPreferences("Hummingbird_on_wheels", Context.MODE_PRIVATE);
        String auth_token = prefs.getString("auth_token", "token_missing");
        return auth_token;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        anime_presenter.onDestroy();
    }

    @Override
    public void onPause(){
        super.onPause();
        anime_presenter.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        anime_presenter.onStop();
    }

    @Override
    public void onResume(){
        super.onResume();
        anime_presenter.onResume();
    }




}
