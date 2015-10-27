package com.example.angel.astroevents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 10/26/15.
 */
public class EventAdapter extends ArrayAdapter<AstronomicalEvent> {
    private Context mContext;
    private ArrayList<AstronomicalEvent> mEvents;
    private LayoutInflater mInflater;

    EventAdapter(Context context, int resourceID, ArrayList<AstronomicalEvent> events){
        super(context, resourceID, events);
        this.mContext = context;
        this.mEvents = events;
        mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
        AstronomicalEvent event = this.mEvents.get(position);
        String text = String.format("%s : %s %s %s %s %s", event.getEvent_name(), event.getMonth(), event.getDay(),
                event.getDay_of_week(), event.getTime(), event.getYear());

        return convertView;
    }

    @Override
    public int getCount(){
        return mEvents.size();
    }

}
