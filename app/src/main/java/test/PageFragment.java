package test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import android.support.v4.app.Fragment;
import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.models.LibraryEntry;
import hummingbird.android.mobile_app.presenters.LibraryAdapter;

/**
 * Created by CzeWen on 2016-01-10.
 */
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static PageFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedOInstanceState){
        super.onCreate(savedOInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.list_view_for_fragment, container, false);
        ListView list_view = (ListView) view.findViewById(R.id.listviewfragment);
        ArrayList<LibraryEntry> library_entries = new ArrayList<LibraryEntry>();//https://static.hummingbird.me/anime/poster_images/000/000/359/large/461736.jpg?1409535570
        LibraryEntry test_entry = new LibraryEntry();
        test_entry.anime = new Anime();
        test_entry.anime.title = "test";
        test_entry.anime.cover_image =  "https://static.hummingbird.me/anime/poster_images/000/000/359/large/461736.jpg?1409535570";
        library_entries.add(test_entry);
        library_entries.add(test_entry);
        LibraryAdapter library_adapter = new LibraryAdapter(getContext(), R.layout.library_entry_item,  library_entries);
        list_view.setAdapter(library_adapter);
        return view;
    }
}
