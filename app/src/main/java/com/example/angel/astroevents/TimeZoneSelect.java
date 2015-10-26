package com.example.angel.astroevents;


import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TimeZoneSelect extends ListActivity {
    String[] timeZones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_zone_select);
        ListView lstView = getListView();
        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lstView.setTextFilterEnabled(true);


        timeZones = getResources().getStringArray(R.array.time_zones);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeZones));
    }

    public void onListItemClick(ListView parent, View v, int position, long id){
        Toast.makeText(this, "(send to list of astro events)", Toast.LENGTH_SHORT).show();
    }
}
