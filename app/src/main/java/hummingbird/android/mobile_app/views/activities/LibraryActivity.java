package hummingbird.android.mobile_app.views.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.presenters.LibraryAdapter;
import hummingbird.android.mobile_app.presenters.LibraryPresenter;
import hummingbird.android.mobile_app.presenters.RetainedPresenter;
import hummingbird.android.mobile_app.views.NavDrawerSetup;
import hummingbird.android.mobile_app.views.fragments.LibraryFragmentAdapter;
import hummingbird.android.mobile_app.views.fragments.LibraryListFragment;

public class LibraryActivity extends AppCompatActivity implements LibraryView,
        LibraryListFragment.OnLibaryListSelectedListener{

    String username;
    String auth_token;
    RetainedPresenter<LibraryPresenter> retained_presenter;
    LibraryPresenter library_presenter;
    LibraryFragmentAdapter fragment_adapter;
    ViewPager view_pager;
    TabLayout library_tabs;
    public boolean first_page_loaded;
    TextView search_filter;
    Spinner search_filter_spinner;
    Spinner filter_by_value_spinner;
    ArrayList<String> filter_by_value_array;
    ArrayAdapter<String> filter_value_adapter;

    NavDrawerSetup nav_drawer;
    private ListView menuDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        Intent extras = getIntent();
        username = extras.getStringExtra("username");
        auth_token = extras.getStringExtra("auth_token");
        first_page_loaded = false;

        FragmentManager fm = getFragmentManager();
        if(fm.findFragmentByTag("presenter")==null){
            library_presenter = new LibraryPresenter(this);
            retained_presenter = new RetainedPresenter<LibraryPresenter>();
            retained_presenter.setData(library_presenter);
            fm.beginTransaction().add(retained_presenter, "presenter").commit();
        }
        else{
            library_presenter = retained_presenter.getData();
        }
        //setup library tabs
        setupTabs();

        //setup left navigation menu
        menuDrawerList = (ListView) findViewById(R.id.left_drawer);
        nav_drawer = new NavDrawerSetup(menuDrawerList, this);

        //set up spinner filter widgets (filtering by extra criteria)
        search_filter_spinner = (Spinner) findViewById(R.id.library_search_filter_spinner);
        filter_by_value_array = new ArrayList<String>();
        filter_by_value_spinner = (Spinner) findViewById(R.id.filter_by_value_spinner);
        filter_value_adapter = new ArrayAdapter<String>(filter_by_value_spinner.getContext(), R.layout.textview_layout, filter_by_value_array);
        filter_by_value_spinner.setAdapter(filter_value_adapter);
        search_filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String genre_type = (String) search_filter_spinner.getItemAtPosition(position);
                updateFilterByValueSpinner(genre_type);
                if(genre_type.contentEquals("")){
                    filter_listings(search_filter);
                }
            }

            public void onNothingSelected(AdapterView<?> parent){
            }
        });
        filter_by_value_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                filter_listings(search_filter);
            }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });
        //set up filter by text widget
        search_filter = (EditText) findViewById(R.id.library_text_filter);
        search_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    getCurrentFragment().libraryAdapter.resetDefault();
                    if(otherFiltersEnabled()){
                        String search_filter_type = (String) search_filter_spinner.getSelectedItem();
                        String filter_value = (String) filter_by_value_spinner.getSelectedItem();
                        ((LibraryAdapter.LibraryFilter<String>) getCurrentFragment().libraryAdapter.getFilter()).setOtherFiltersValue(search_filter_type, filter_value);
                        getCurrentFragment().libraryAdapter.getFilter().filter("");
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_library, menu);
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

    public void setupTabs(){
        view_pager = (ViewPager) findViewById(R.id.library_view_pager);
        library_tabs = (TabLayout)findViewById(R.id.library_tabs);
        fragment_adapter = new LibraryFragmentAdapter(getSupportFragmentManager(), LibraryActivity.this);
        view_pager.setAdapter(fragment_adapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                fragment_adapter.onFragmentSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        library_tabs.setupWithViewPager(view_pager);
        int a = 1;
    }

    public void fetchLibraryInformation(){
        if(username.contentEquals("me")){
            library_presenter.fetchLibraryInformation(username, auth_token);
        }
        else{
            library_presenter.fetchLibraryInformation(username);
        }
    }

    public LibraryListFragment getCurrentFragment(){
        return (LibraryListFragment) fragment_adapter.getRegisteredFragment(view_pager.getCurrentItem());
    }

    public LibraryListFragment getFragment(String name){
        return (LibraryListFragment) fragment_adapter.getRegisteredFragment(name);
    }

    public void firstFragmentLoaded(){
        if(first_page_loaded==false){
            fragment_adapter.onFragmentSelected(0);
            first_page_loaded = true;
        }
    }

    public void filter_listings(View view){
        if(search_filter==null){
            search_filter = (TextView) findViewById(R.id.library_text_filter);
        }
        getCurrentFragment().libraryAdapter.resetDefault();
        CharSequence pattern = search_filter.getText().toString();
        LibraryListFragment current_fragment = (LibraryListFragment) fragment_adapter.getRegisteredFragment(view_pager.getCurrentItem());
        if(otherFiltersEnabled()){
            String filter_type = (String) search_filter_spinner.getSelectedItem();
            String filter_value = (String) filter_by_value_spinner.getSelectedItem();
            ((LibraryAdapter.LibraryFilter<String>) getCurrentFragment().libraryAdapter.getFilter()).setOtherFiltersValue(filter_type, filter_value);
        }
        else{
            ((LibraryAdapter.LibraryFilter<String>) getCurrentFragment().libraryAdapter.getFilter()).setOtherFiltersValue(null, null);
        }
        current_fragment.libraryAdapter.getFilter().filter(pattern);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        library_presenter.onDestroy();
    }

    @Override
    public void onResume(){
        super.onResume();
        library_presenter.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        library_presenter.onPause();
    }

    public void updateListWithAnimeActivityUpdates(int id, HashMap<String, String> changes){
        library_presenter.updateOldLibraryEntry(id, changes);
    }

    public void updateFilterByValueSpinner(String genre_type){

        int resource_id;
        switch(genre_type.toLowerCase()){
            case "genre":
                resource_id = R.array.genre_values;
                break;
            case "rating":
                resource_id = R.array.rating_values;
                break;
            case "status":
                resource_id = R.array.airing_status_values;
                break;
            default:
                filter_value_adapter.clear();
                filter_value_adapter.notifyDataSetChanged();
                return;
        }
        filter_by_value_array.clear();
        String[] new_spinner_values = getResources().getStringArray(resource_id);
        filter_by_value_array.addAll(Arrays.asList(new_spinner_values));
        filter_value_adapter.notifyDataSetChanged();
    }

    public boolean otherFiltersEnabled(){
        if(search_filter_spinner ==  null)
            search_filter_spinner = (Spinner) findViewById(R.id.library_search_filter_spinner);
        if(filter_by_value_spinner == null)
            filter_by_value_spinner = (Spinner) findViewById(R.id.filter_by_value_spinner);
        String filter_type = (String) search_filter_spinner.getSelectedItem();
        if(filter_type.contentEquals(""))
            return false;
        return true;
    }

}
