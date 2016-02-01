package hummingbird.android.mobile_app.presenters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import android.widget.Filter;

import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.models.LibraryEntry;

/**
 * Created by CzeWen on 2016-01-09.
 */
public class LibraryAdapter extends ArrayAdapter<LibraryEntry> implements Filterable {
    Context context;
    int layoutResourceId;
    ArrayList<LibraryEntry> data =  null;
    public ArrayList<LibraryEntry> original = null;
    LibraryFilter<String> search_filter;

    public LibraryAdapter(Context context, int layoutResourceId, ArrayList<LibraryEntry> data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        original = new ArrayList<LibraryEntry>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder view_holder = null;
        View row = convertView;
        //LayoutInflater inflater =  LayoutInflater.from(super.getContext());
        //LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            view_holder = new ViewHolder();
            view_holder.library_entry_image = (ImageView) row.findViewById(R.id.library_entry_image);
            view_holder.title = (TextView) row.findViewById(R.id.library_entry_title);

            row.setTag(view_holder);
        }
        else{
            view_holder = (ViewHolder) convertView.getTag();
        }
        LibraryEntry entry = data.get(position);
        String image_uri = entry.anime.cover_image;
        Picasso.with(row.getContext())
                .load(image_uri)
                .fit()
                .centerCrop()
                .into(view_holder.getLibraryEntryImage());
        view_holder.getTitle().setText(entry.anime.title);
        return row;
    }

    @Override
    public Filter getFilter(){
        if(search_filter==null){
            search_filter = new LibraryFilter<String>();
        }
        return search_filter; //stub wip
    }

    private class LibraryFilter<E> extends Filter{


        @Override
        public FilterResults performFiltering(CharSequence constraint){
            FilterResults filter_results = new FilterResults();
            //if(constraint == null || constraint.length()==0){
             //   filter_results.values = data;
             //   filter_results.count = data.size();
            //}
            if(constraint!=null && constraint.length()>0){
                ArrayList<LibraryEntry> filtered_entries = new ArrayList<>();
                for(LibraryEntry entry : original){
                    if(entry.anime.title.toLowerCase().contains(constraint))
                        filtered_entries.add(entry);
                }
                filter_results.values = filtered_entries;
                filter_results.count = filtered_entries.size();
            }
            return filter_results;
        }

        @Override
        public void publishResults(CharSequence constraint, FilterResults results){
            ArrayList<LibraryEntry> temp = data;
            ArrayList<LibraryEntry> matching_entries = (ArrayList<LibraryEntry>) results.values;
            if(matching_entries!=null) {
                data.clear();
                for(LibraryEntry entry : matching_entries){
                    data.add(entry);
                }
            }
            notifyDataSetChanged();
        }

    }

    public void resetDefault(){
        this.clear();
        for(LibraryEntry entry : original){
            this.add(entry);
        }
        notifyDataSetChanged();
    }

    class ViewHolder{

        ImageView library_entry_image;
        TextView title;

        public ImageView getLibraryEntryImage(){
            //if(this.library_entry_image == null){
               // library_entry_image = (ImageView) row.findViewById(R.id.library_entry_image);
            //}
            return library_entry_image;
        }

        public TextView getTitle(){
            //if(this.title == null){
             //   title = (TextView) row.findViewById(R.id.title);
            //}
            return title;

        }
    }

    public void copyToOriginal(ArrayList<LibraryEntry> entries){
        original.clear();
        for(LibraryEntry entry : entries){
            original.add(entry);
        }
    }


}
