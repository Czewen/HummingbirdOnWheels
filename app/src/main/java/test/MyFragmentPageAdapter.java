package test;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.models.LibraryEntry;
import hummingbird.android.mobile_app.presenters.LibraryAdapter;

/**
 * Created by CzeWen on 2016-01-10.
 */
public class MyFragmentPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
    private Context context;


    public MyFragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public View getTabView(int position){
        View v = LayoutInflater.from(context).inflate(R.layout.list_view_for_fragment, null);
        ListView list_view = (ListView) v.findViewById(R.id.listviewfragment);
        ArrayList<LibraryEntry> library_entries = new ArrayList<LibraryEntry>();//https://static.hummingbird.me/anime/poster_images/000/000/359/large/461736.jpg?1409535570
        LibraryEntry test_entry = new LibraryEntry();
        test_entry.anime = new Anime();
        test_entry.anime.title = "test";
        test_entry.anime.cover_image =  "https://static.hummingbird.me/anime/poster_images/000/000/359/large/461736.jpg?1409535570";
        library_entries.add(test_entry);
        LibraryAdapter library_adapter = new LibraryAdapter(context, R.layout.library_entry_item,  library_entries);
        list_view.setAdapter(library_adapter);
        return v;
    }

}
