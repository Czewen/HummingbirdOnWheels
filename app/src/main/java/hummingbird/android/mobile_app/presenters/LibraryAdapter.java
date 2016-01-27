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
    public LibraryAdapter(Context context, int layoutResourceId, ArrayList<LibraryEntry> data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
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
        return null; //stub wip
    }

    private class LibraryFilter<E> extends Filter{

        String addtional_constraint_type;
        Comparable<E> value;

        public LibraryFilter(String additional_constraint_type, Comparable<E> value){
            this.addtional_constraint_type = additional_constraint_type;
            this.value = value;
        }

        @Override
        public FilterResults performFiltering(CharSequence constraint){
            FilterResults filter_results = new FilterResults();
            if(constraint == null || constraint.length()==0){
                filter_results.values = data;
                filter_results.count = data.size();
            }
            else{
                ArrayList<LibraryEntry> filtered_entries = new ArrayList<>();
                for(LibraryEntry entry : data){
                    if(entry.anime.title.contains(constraint))
                        filtered_entries.add(entry);
                }
                filter_results.values = data;
                filter_results.count = data.size();
            }
            return filter_results;
        }

        @Override
        public void publishResults(CharSequence constraint, FilterResults results){
            ArrayList<LibraryEntry> temp = data;
            data = (ArrayList<LibraryEntry>) results.values;
            notifyDataSetChanged();
        }

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



}
