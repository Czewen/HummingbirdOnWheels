package hummingbird.android.mobile_app.views.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hummingbird.android.mobile_app.models.LibraryEntry;
import test.PageFragment;

/**
 * Created by CzeWen on 2016-01-12.
 */
public class ProfileFragmentPagerAdapter extends FragmentPagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private String[] tabTitles = { "Profile", "Library" };
    final int PAGE_COUNT = 2;
    private Context context;

    public ProfileFragmentPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
        List<Fragment> alive_fragments = fm.getFragments();
        if(alive_fragments!=null){
            int i = 0;
            for(Fragment fragment : alive_fragments){
                registeredFragments.put(i, fragment);
                i++;
            }
        }
    }

    @Override
    public int getCount(){
        return PAGE_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj){
        registeredFragments.remove(position);
        super.destroyItem(container, position, obj);
    }

    @Override
    public Fragment getItem(int position){
        String page_type = tabTitles[position];
        if(page_type.contentEquals("Library")){
            return LibraryListFragment.newInstance(page_type, position);
        }
        return PageFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public Fragment getRegisteredFragment(int position){
        return registeredFragments.get(position);
    }

    public String getTabTitle(int position){
        return tabTitles[position];
    }


    public void onFragmentSelected(int position){
        Fragment fragment;
        if(registeredFragments.get(position) == null) {
            fragment = getItem(position);
        }
        else{
            fragment = registeredFragments.get(position);
        }
        if(fragment.getClass().equals(LibraryListFragment.class)){
            ((LibraryListFragment) fragment).mCallBack.fetchLibraryInformation();
        }
    }


}
