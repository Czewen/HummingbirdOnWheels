package hummingbird.android.mobile_app.presenters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hummingbird.android.mobile_app.R;
import java.util.ArrayList;
import hummingbird.android.mobile_app.models.Anime;

/**
 * Created by CzeWen on 2016-05-17.
 */
public class SearchAnimeAdapter extends ArrayAdapter<Anime>{
    Context context;
    int layoutResourceId;
    ArrayList<Anime> data =  null;

    class ViewHolder{

        ImageView search_result_image;
        TextView title;
        TextView show_type;
        TextView rating;
        TextView episodes;
        TextView description;

        public ImageView getSearchResultImage(){return search_result_image;}

        public TextView getTitle(){return title;}

        public TextView getShowType(){return show_type;}

        public TextView getRating(){return rating;}

        public TextView getEpisodes(){return episodes;}

        public TextView getDescription(){return description;}
    }

    public SearchAnimeAdapter(Context context, int layoutResourceId, ArrayList<Anime> data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder view_holder;
        View row = convertView;
        if(convertView == null){
            LayoutInflater layout_inflater = ((Activity)context).getLayoutInflater();
            row = layout_inflater.inflate(layoutResourceId, parent, false);
            view_holder = new ViewHolder();
            view_holder.search_result_image = (ImageView)row.findViewById(R.id.search_result_image);
            view_holder.title = (TextView) row.findViewById(R.id.search_result_title);
            view_holder.show_type = (TextView) row.findViewById(R.id.search_result_show_type);
            view_holder.episodes = (TextView) row.findViewById(R.id.search_result_episodes);
            view_holder.rating = (TextView) row.findViewById(R.id.search_result_rating);
            view_holder.description = (TextView) row.findViewById(R.id.search_result_description);
            row.setTag(view_holder);
        }
        else{
            view_holder = (ViewHolder) convertView.getTag();
        }
        Anime search_result = data.get(position);
        Picasso.with(row.getContext())
                .load(search_result.cover_image)
                .fit()
                .centerCrop()
                .into(view_holder.getSearchResultImage());
        TextView title_box = view_holder.getTitle();
        title_box.setText(search_result.title);
        String text_in_titlebox = title_box.getText().toString();
        if(text_in_titlebox.length() < search_result.title.length()){
            String shortened_name = text_in_titlebox.substring(0, text_in_titlebox.length()-3);
            shortened_name+="...";
            title_box.setText(shortened_name);
        }
        view_holder.getShowType().setText(search_result.show_type);
        view_holder.getEpisodes().setText("Episodes: " + search_result.episode_count);
        view_holder.getRating().setText("Rating: "+search_result.community_rating);
        view_holder.getDescription().setText(search_result.sypnopsis);
        String description = search_result.toString();
        return row;
    }



}
