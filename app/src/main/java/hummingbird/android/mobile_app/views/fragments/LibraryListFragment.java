package hummingbird.android.mobile_app.views.fragments;

/**
 * Created by CzeWen on 2016-01-12.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.support.v4.app.Fragment;
import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.models.Anime;
import hummingbird.android.mobile_app.models.LibraryEntry;
import hummingbird.android.mobile_app.presenters.LibraryAdapter;
import hummingbird.android.mobile_app.views.activities.AnimeActivity;

public class LibraryListFragment extends Fragment {

    public static final String ARG_ENTRIES = "ARG_ENTRIES";
    public ArrayList<LibraryEntry> library_entries = new ArrayList<>();
    public LibraryAdapter libraryAdapter;
    private ListView list_view;
    private String list_type;
    int position;


    OnLibaryListSelectedListener mCallBack;

    public interface OnLibaryListSelectedListener{
        public void fetchLibraryInformation();

        public void firstFragmentLoaded();

        public void updateLisWithAnimeActivityUpdates(int id, HashMap<String, String> changes);
    }


    public static LibraryListFragment newInstance(String list_type, int position){
        Bundle args = new Bundle();
        args.putString("list_type", list_type);
        args.putInt("position", position);
        //args.putParcelableArrayList(ARG_ENTRIES, entries);
        LibraryListFragment fragment = new LibraryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            Activity activity = (Activity) context;
            mCallBack = (OnLibaryListSelectedListener) activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ " must implement OnLibaryListSelectedListener interface");
        }
    }


    @Override
    public void onCreate(Bundle saved_instance_data){
        super.onCreate(saved_instance_data);
        Bundle args = getArguments();
        list_type = args.getString("list_type");
        position = args.getInt("position");
        //library_entries = saved_instance_data.getParcelableArrayList(ARG_ENTRIES);
    }

    @Override
    public View onCreateView(LayoutInflater layout_inflater, ViewGroup parent, Bundle saved_instance_state){
        View view = layout_inflater.inflate(R.layout.list_view_for_fragment, parent, false);
        list_view = (ListView) view.findViewById(R.id.listviewfragment);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AnimeActivity.class);
                LibraryEntry entry = (LibraryEntry) list_view.getItemAtPosition(position);
                intent.putExtra("LibraryEntry", entry);
                intent.putExtra("id", entry.anime.id);
                intent.putExtra("isLibraryEntry", true);
                intent.putExtra("LibraryEntryPosition", position);
                startActivityForResult(intent, 1);
            }
        });
        libraryAdapter = new LibraryAdapter(getContext(), R.layout.library_entry_item, library_entries);
        list_view.setAdapter(libraryAdapter);
        mCallBack.firstFragmentLoaded();
        return view;
    }

    public void populateList(Bundle list_data){
        if(library_entries==null){
            library_entries = new ArrayList<>();
        }
        ArrayList<LibraryEntry> entries = list_data.getParcelableArrayList(ARG_ENTRIES);
        library_entries.clear();
        for(LibraryEntry entry : entries){
            library_entries.add(entry);
        }
        libraryAdapter.copyToOriginal(entries);
        libraryAdapter.notifyDataSetChanged();
    }

    public void setListType(String list_type){
        this.list_type = list_type;
    }

    public String getListType(){
        return this.list_type;
    }

    public boolean hasEntries(){
        boolean result = (library_entries.size()==0) ? false : true;
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            int library_entry_position = extras.getInt("LibraryEntryPosition");
            LibraryEntry entry_to_update = libraryAdapter.getItem(library_entry_position);
            HashMap<String, String> successful_updates = (HashMap<String, String>)extras.getSerializable("LibraryEntryUpdates");
            boolean remove_entry = false;
            for(Map.Entry<String, String> entry : successful_updates.entrySet()){
                switch(entry.getKey()){
                    case "status":
                        //since the entry does not match the current filter we have to remove it from view
                        if(!list_type.toLowerCase().contentEquals("all"))
                            remove_entry = true;
                        break;
                    case "episodes_watched":
                        entry_to_update.episodes_watched = Integer.parseInt(entry.getValue());
                        break;
                    case "rating":
                        entry_to_update.rating.value = Float.parseFloat(entry.getValue());
                        break;
                }
            }//end for loop
            if(remove_entry) {
                library_entries.remove(entry_to_update);
                libraryAdapter.remove(entry_to_update);
            }
            //rerender updated library entry view in library list fragment
            libraryAdapter.notifyDataSetChanged();
        }
    }





}
