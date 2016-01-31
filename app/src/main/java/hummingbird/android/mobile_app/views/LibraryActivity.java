package hummingbird.android.mobile_app.views;

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
import android.widget.EditText;
import android.widget.TextView;

import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.presenters.LibraryPresenter;
import hummingbird.android.mobile_app.presenters.RetainedPresenter;
import hummingbird.android.mobile_app.views.activities.LibraryView;
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
            fm.beginTransaction().add(retained_presenter, "data").commit();
        }
        else{
            library_presenter = retained_presenter.getData();
        }
        setupTabs();
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
                if(s.length()==0){
                    getCurrentFragment().libraryAdapter.resetDefault();
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
        int b = 1;
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
        CharSequence pattern = search_filter.getText().toString();
        LibraryListFragment current_fragment = (LibraryListFragment) fragment_adapter.getRegisteredFragment(view_pager.getCurrentItem());
        current_fragment.libraryAdapter.getFilter().filter(pattern);
    }

}
