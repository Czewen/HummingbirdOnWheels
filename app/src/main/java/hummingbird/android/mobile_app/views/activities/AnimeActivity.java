package hummingbird.android.mobile_app.views.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.presenters.AnimePresenter;
import hummingbird.android.mobile_app.presenters.ProfilePresenter;
import hummingbird.android.mobile_app.presenters.RetainedPresenter;

/**
 * Created by CzeWen on 2016-01-18.
 */
public class AnimeActivity extends AppCompatActivity implements AnimeView {

    int id;
    Anime anime_obj;
    AnimePresenter anime_presenter;
    RetainedPresenter<AnimePresenter> retained_presenter;

    ImageView cover_photo;
    TextView title_view;
    TextView episodes_watched;
    TextView no_episodes;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);
        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        FragmentManager fm = getFragmentManager();
        retained_presenter = (RetainedPresenter<AnimePresenter>)fm.findFragmentByTag("data");
        if(retained_presenter == null){
            retained_presenter = new RetainedPresenter<AnimePresenter>();
            anime_presenter = new AnimePresenter(this);
            fm.beginTransaction().add(retained_presenter, "data");
            retained_presenter.setData(anime_presenter);
        }
        else{
            anime_presenter = retained_presenter.getData();
        }
        anime_presenter.fetchAnime(id);


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
        no_episodes.setText("/"+episode_count);
        //String number_of_episodes = "/"+episode_count;
    }

    public void increaseWatchedEpisodes(View view){
        if(episodes_watched == null){
            episodes_watched = (TextView) findViewById(R.id.episodes_watched);
        }
        String previous_sum = episodes_watched.getText().toString();
        int new_count = Integer.parseInt(previous_sum) + 1;
        episodes_watched.setText(Integer.toString(new_count));

    }


}
