package hummingbird.android.mobile_app.views.activities;

/**
 * Created by CzeWen on 2016-01-18.
 */
public interface AnimeView {

    public void setCoverPhoto(String url);

    public void setTitle(String title);

    public void setEpisodeCount(int episode_count);

    public void setEpisodesWatched(int episodes_watched_value);
}
