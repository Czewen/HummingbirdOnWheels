package hummingbird.android.mobile_app.views.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.presenters.RetainedPresenter;
import hummingbird.android.mobile_app.presenters.SearchAnimeAdapter;
import hummingbird.android.mobile_app.presenters.SearchPresenter;
import hummingbird.android.mobile_app.views.NavDrawerSetup;

public class SearchActivity extends Activity implements SearchView{


    NavDrawerSetup nav_drawer;
    ListView menuDrawerList;
    ListView search_results_list;
    SearchAnimeAdapter search_anime_adapter;
    SearchPresenter searchPresenter;
    TextView searchbox;
    RetainedPresenter<SearchPresenter> retained_presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //get references to widgets
        FragmentManager fm = getFragmentManager();
        if(fm.findFragmentByTag("presenter")==null){
            searchPresenter = new SearchPresenter(this);
            retained_presenter = new RetainedPresenter<SearchPresenter>();
            retained_presenter.setData(searchPresenter);
            fm.beginTransaction().add(retained_presenter, "presenter").commit();
        }
        else{
            searchPresenter = retained_presenter.getData();
        }
        searchbox = (EditText)findViewById(R.id.searchbox);
        menuDrawerList = (ListView) findViewById(R.id.left_drawer);
        search_results_list = (ListView) findViewById(R.id.search_results_list);

        nav_drawer = new NavDrawerSetup(menuDrawerList, this);

        //setup adapters for listview for search results
        search_anime_adapter = new SearchAnimeAdapter(this, R.layout.search_result_layout, new ArrayList<Anime>());
        search_results_list.setAdapter(search_anime_adapter);

        //setup listeners for searchbox
        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    clearSearchResults();
                }
            }
        });
        searchbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //null keyevent when enter key is pressed (search button in this scenario)
                if(actionId == EditorInfo.IME_ACTION_SEARCH && event == null){
                    searchPresenter.searchAnimeByTitle(searchbox.getText().toString());
                    String text = searchbox.getText().toString();
                }
                return false;
            }
        });
    }

        //setup click handlers for search results


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showSearchResults(ArrayList<Anime> results){
        clearSearchResults();
        search_anime_adapter.addAll(results);
        search_anime_adapter.notifyDataSetChanged();
    }

    public void clearSearchResults() {
        search_anime_adapter.clear();
        search_anime_adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        searchPresenter.onDestroy();
    }

    @Override
    public void onResume(){
        super.onResume();
        searchPresenter.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        searchPresenter.onPause();
    }
}
