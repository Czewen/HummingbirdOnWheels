package hummingbird.android.mobile_app.views.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import hummingbird.android.mobile_app.Api.helper.CircleTransform;
import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.presenters.ProfilePresenter;
import hummingbird.android.mobile_app.presenters.RetainedPresenter;
import hummingbird.android.mobile_app.views.NavDrawerSetup;
import hummingbird.android.mobile_app.views.ProfileView;
import hummingbird.android.mobile_app.views.fragments.LibraryListFragment;
import hummingbird.android.mobile_app.views.fragments.ProfileFragmentPagerAdapter;

public class ProfileActivity extends AppCompatActivity implements ProfileView,
        LibraryListFragment.OnLibaryListSelectedListener {

    ProfilePresenter profile_presenter;
    private RetainedPresenter<ProfilePresenter> retained_presenter;
    TabLayout profile_tabs;
    ViewPager view_pager;
    ProfileFragmentPagerAdapter fragment_pager_adapter;
    String username;
    ImageView cover_photo_widget;
    RelativeLayout cover_photo_layout;
    private String auth_token;

    private ListView menuDrawerList;
    private DrawerLayout menuDrawerLayout;
    private String[] left_drawer_items;
    NavDrawerSetup nav_drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            auth_token = savedInstanceState.getString("auth_token");
            username = savedInstanceState.getString("user");
        }
        FragmentManager fm = getFragmentManager();
        retained_presenter = (RetainedPresenter<ProfilePresenter>)fm.findFragmentByTag("data");
        if(retained_presenter == null){
            profile_presenter = new ProfilePresenter(this);
            retained_presenter = new RetainedPresenter<ProfilePresenter>();
            retained_presenter.setData(profile_presenter);
            fm.beginTransaction().add(retained_presenter, "data").commit();
        }
        else{
            profile_presenter = retained_presenter.getData();
        }
        profile_presenter.bindView(this);
        if(!profile_presenter.isListening())
            profile_presenter.onResume();
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        setContentView(R.layout.activity_profile);
        setupTabs();
        SharedPreferences prefs = getSharedPreferences("Hummingbird_on_wheels", Context.MODE_PRIVATE);
        auth_token = prefs.getString("auth_token", "token_missing");
        profile_presenter.getProfileInfo(username);
            //profile_presenter.getOwnLibrary(auth_token);


        //get references to widgets
        menuDrawerList = (ListView) findViewById(R.id.left_drawer);
        nav_drawer = new NavDrawerSetup(menuDrawerList, this);

    }



    public void setupTabs(){
        profile_tabs = (TabLayout)findViewById(R.id.profile_tabs);
        view_pager = (ViewPager) findViewById(R.id.profile_view_pager);
        fragment_pager_adapter = new ProfileFragmentPagerAdapter(getSupportFragmentManager(), ProfileActivity.this);
        view_pager.setAdapter(fragment_pager_adapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                fragment_pager_adapter.onFragmentSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        profile_tabs.setupWithViewPager(view_pager);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        profile_presenter.bindView(this);
        auth_token = savedInstanceState.getString("auth_token");
        username = savedInstanceState.getString("user");
    }


    public void setName(String name){
        TextView name_field = (TextView) findViewById(R.id.profile_name);
        name_field.setText(name);
        name_field.setTextColor(0xffffffff);
    }

    public void setBio(String bio){

    }

    public void setProfile_Avatar(String image_url){
        ImageView avatar_widget = (ImageView) findViewById(R.id.user_profile_image);
        Picasso.with(this).load(image_url)
                .transform(new CircleTransform())
                .into(avatar_widget);
    }

    public void setCover_image(String image_url){
        if(cover_photo_widget == null){
            cover_photo_widget = (ImageView) findViewById(R.id.cover_photo);
        }
        Picasso.with(this).load(image_url)
                .fit()
                .centerCrop()
                .into(cover_photo_widget);
    }

    public ProfileFragmentPagerAdapter getFragmentPagerAdapter(){
       return fragment_pager_adapter;
    }

    public Fragment getCurrentFragment(){
        return fragment_pager_adapter.getRegisteredFragment(view_pager.getCurrentItem());
    }

    public ViewPager getView_pager(){
        return view_pager;
    }

    public String getCurrentUser(){
        return username;
    }

    public void fetchLibraryInformation(){
       if(username.contentEquals("me")){
           profile_presenter.getOwnLibrary(auth_token);
       }
        else{

       }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        profile_presenter.unbindView();
        retained_presenter.setData(profile_presenter);
        profile_presenter.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString("auth_token", auth_token);
        savedInstanceState.putString("user", username);
        profile_presenter.unbindView();
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onPause(){
        super.onPause();
        profile_presenter.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        profile_presenter.onStop();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!profile_presenter.isListening())
            profile_presenter.onResume();
        profile_presenter.bindView(this);
    }

    public Context getContext(){
        return getContext();
    }

    public Activity getActivity(){
        return this;
    }

    public void setResponseError(String error){
        TextView a = (TextView) findViewById(R.id.response_error);
        a.setText(error);
        a.setTextColor(0xffffffff);
    }

    public void getProfileInformation(View view){
        profile_presenter.getProfileInfo("MrWen");
    }

    public void openLibrary(View view){
        Intent intent = new Intent(this, LibraryActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("auth_token", auth_token);
        startActivity(intent);
    }

    public void firstFragmentLoaded(){
        //STUB
    }

    public void updateListWithAnimeActivityUpdates(int id, HashMap<String, String> changes){
       //STUB
    }
}
