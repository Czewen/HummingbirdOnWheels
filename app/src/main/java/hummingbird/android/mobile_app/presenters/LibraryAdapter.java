package hummingbird.android.mobile_app.presenters;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import android.widget.Filter;
import android.app.Activity;

import hummingbird.android.mobile_app.CustomLayouts.RowLayout;
import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.models.Genre;
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
    //convertView is an old view that is no longer used
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
            view_holder.library_entry_genre_container = (RowLayout) row.findViewById(R.id.library_entry_genre);
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
        ArrayList<Genre> genre_array = entry.anime.genres;
        addLibraryEntryGenreValues(entry.anime.genres, view_holder.getLibraryEntryGenreContainer());
        return row;
    }

    @Override
    public Filter getFilter(){
        if(search_filter==null){
            search_filter = new LibraryFilter<String>();
        }
        return search_filter; //stub wip
    }

    public class LibraryFilter<E> extends Filter{

        private String extra_filter_type = null;
        private String extra_filter_value = null;

        @Override
        public FilterResults performFiltering(CharSequence constraint){
            FilterResults filter_results = new FilterResults();
            //if(constraint == null || constraint.length()==0){
             //   filter_results.values = data;
             //   filter_results.count = data.size();
            //}
            if(hasConstraints(constraint)){
                ArrayList<LibraryEntry> filtered_entries = new ArrayList<>();
                for(LibraryEntry entry : original){
                    if(constraint!=null && constraint.length()>0 && !(entry.anime.title.toLowerCase().contains(constraint)))
                        continue;
                    if(extra_filter_type!=null && matchesExtraConstraint(entry) == false)
                        continue;
                        filtered_entries.add(entry);
                }
                filter_results.values = filtered_entries;
                filter_results.count = filtered_entries.size();
            }
//            if(constraint!=null && constraint.length()>0){
//                ArrayList<LibraryEntry> filtered_entries = new ArrayList<>();
//                for(LibraryEntry entry : original){
//                    if(entry.anime.title.toLowerCase().contains(constraint))
//                        filtered_entries.add(entry);
//                }
//                filter_results.values = filtered_entries;
//                filter_results.count = filtered_entries.size();
//            }
            return filter_results;
        }

        @Override
        public void publishResults(CharSequence constraint, FilterResults results){
            ArrayList<LibraryEntry> matching_entries = (ArrayList<LibraryEntry>) results.values;
            if(matching_entries!=null) {
                data.clear();
                for(LibraryEntry entry : matching_entries){
                    data.add(entry);
                }
            }
            notifyDataSetChanged();
        }

        public void setOtherFiltersValue(String filter_type, String filter_value){
            extra_filter_type = filter_type;
            extra_filter_value = filter_value;
        }

        private boolean hasConstraints(CharSequence constraint){
            if((constraint==null || constraint.length()==0) && extra_filter_type == null)
                return false;
            return true;
        }

        private boolean matchesExtraConstraint(LibraryEntry entry){
            switch(extra_filter_type.toLowerCase()){
                case "genre":
                    for(Genre genre : entry.anime.genres){
                        if(genre.name.toLowerCase().contains(extra_filter_value.toLowerCase()))
                            return true;
                    }
                    break;
                case "rating":
                    return true;

                case "status":
                    return true;
            }
            return false;
        }


    }

    public void resetDefault(){
        data.clear();
        this.clear();
        this.addAll(original);
        notifyDataSetChanged();
    }

    class ViewHolder{

        ImageView library_entry_image;
        TextView title;
        RowLayout library_entry_genre_container;

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

        public RowLayout getLibraryEntryGenreContainer(){
            return library_entry_genre_container;
        }
    }

    public void copyToOriginal(ArrayList<LibraryEntry> entries){
        original.clear();
        for(LibraryEntry entry : entries){
            original.add(entry);
        }
    }


    public int dp_to_pixels(int dp){
        //px = dp * (dpi / 160)
        //logicalDensity = dpi/160
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float logicalDensity = metrics.density;
        return (int) Math.ceil(dp * logicalDensity);
    }

    public void addLibraryEntryGenreValues(ArrayList<Genre> genres, RowLayout container){
        container.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        int px = dp_to_pixels(5);
        layoutParams.setMargins(0, 0, px, 0);
        for(Genre genre : genres){
            TextView genre_value = new TextView(getContext());
            genre_value.setText(genre.name);
            genre_value.setTextSize(14);
            container.addView(genre_value, layoutParams);
        }
    }


}
