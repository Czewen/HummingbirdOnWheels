package hummingbird.android.mobile_app.views;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ListView;

import hummingbird.android.mobile_app.views.fragments.ProfileFragmentPagerAdapter;

/**
 * Created by CzeWen on 2016-01-03.
 */
public interface ProfileView {

    public void setProfile_Avatar(String image_url);

    public void setName(String name);

    public void setBio(String bio);

    public void setCover_image(String cover_image_url);

    public Context getContext();

    public Activity getActivity();

    public void setResponseError(String error);

    public String getCurrentUser();

    public Fragment getCurrentFragment();




}
