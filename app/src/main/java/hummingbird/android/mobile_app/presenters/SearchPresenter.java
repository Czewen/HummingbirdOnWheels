package hummingbird.android.mobile_app.presenters;

import android.app.usage.UsageEvents;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import hummingbird.android.mobile_app.Api.services.SearchService;
import hummingbird.android.mobile_app.events.SearchAnimeEvent;
import hummingbird.android.mobile_app.events.SearchAnimeSuccessEvent;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.views.activities.SearchView;

/**
 * Created by CzeWen on 2016-05-17.
 */
public class SearchPresenter extends Presenter {

    SearchView view;
    SearchService search_service;
    ArrayList<Anime> results;
    //only store 15 search results for now
    int max_result_size = 15;


    public SearchPresenter(SearchView view){
        this.view = view;
        search_service = new SearchService();
        super.bindService(search_service);
        EventBus.getDefault().register(this);
    }

    public void searchAnimeByTitle(String title){
        EventBus.getDefault().post(new SearchAnimeEvent(title));
    }

    public void onEvent(SearchAnimeSuccessEvent event){
        results = event.getResults();
        view.showSearchResults(results);
    }
}
