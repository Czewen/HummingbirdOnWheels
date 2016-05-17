package hummingbird.android.mobile_app.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.Arrays;

import hummingbird.android.mobile_app.R;
import hummingbird.android.mobile_app.views.activities.LibraryActivity;
import hummingbird.android.mobile_app.views.activities.ProfileActivity;

/**
 * Created by CzeWen on 2016-05-17.
 */
public class NavDrawerSetup {

    ListView nav_drawer;
    Activity current_activity;
    String[] nav_drawer_items;
//    private String[] nav_drawer_item_mapping = {"Profile", "Library", "Search"};
    private int selected_item_index;

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            String current_activity_title = current_activity.getTitle().toString();
            if(!current_activity_title.contentEquals(nav_drawer_items[position]))
                selectItem(position);
        }
    }

    public NavDrawerSetup(ListView nav_drawer, Activity current_activity){
        this.nav_drawer = nav_drawer;
        this.current_activity = current_activity;
        nav_drawer_items = current_activity.getResources().getStringArray(R.array.left_menu_items);
        selected_item_index = Arrays.asList(nav_drawer_items).indexOf(current_activity.getTitle().toString());

        ArrayAdapter<String> highlight_list_view = new ArrayAdapter<String>(current_activity, R.layout.nav_drawer_item_layout, nav_drawer_items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                final View renderer = super.getView(position, convertView, parent);
                if (position == selected_item_index)
                {
                    //TODO: set the proper selection color here:
                    renderer.setBackgroundResource(android.R.color.holo_orange_light);
                }
                return renderer;
            }
        };
        nav_drawer.setAdapter(highlight_list_view);
        nav_drawer.setOnItemClickListener(new DrawerItemClickListener());
    }

    public void selectItem(int position){
        String next_activity = nav_drawer_items[position];
        Bundle extras = current_activity.getIntent().getExtras();
        Intent intent;
        SharedPreferences prefs = current_activity.getSharedPreferences("Hummingbird_on_wheels", Context.MODE_PRIVATE);
        String auth_token = prefs.getString("auth_token", "token_missing");
        String username = extras.getString("username");
        switch(next_activity){
            case "Profile":
                intent = new Intent(current_activity, ProfileActivity.class);
                break;
            case "Library":
                intent = new Intent(current_activity, LibraryActivity.class);
                break;
            case "Search":
                intent = new Intent(current_activity, LibraryActivity.class);
                break;
            default:
                return;

        }
        intent.putExtra("username", username);
        intent.putExtra("auth_token", auth_token);
        current_activity.startActivity(intent);
    }




}
