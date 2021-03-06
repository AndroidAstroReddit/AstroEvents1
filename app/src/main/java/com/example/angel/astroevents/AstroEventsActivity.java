package com.example.angel.astroevents;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.VideoView;



import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/*
  The list activity that is the second screen after the splash video screen. This class reads from a file in the
  raw folder a json file that has Astronomical Activities.
 */

public class AstroEventsActivity extends ListActivity {
    public static final String DETAIL_NAME_STRING = "com.example.angel.astroevents.astroeventsactivity.detail_name_string";
    public static final String DETAIL_DATE_STRING = "com.example.angel.astroevnets.astoreventsactivty.detail_date_string";

    VideoView mVideoView;

    ArrayList<AstronomicalEvent> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro_events);


        ListView lstView = getListView();
        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lstView.setTextFilterEnabled(true);


        BufferedReader reader = null;
        try {
            InputStream in = this.getResources().openRawResource(R.raw.all_of_them3);
            reader = new BufferedReader(new InputStreamReader(in));
            events = new ArrayList<AstronomicalEvent>();
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);


                JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                        .nextValue();
                Calendar calendar = Calendar.getInstance();
                Log.i("Month", calendar.get(Calendar.YEAR)+"");
                for (int i = 0; i < array.length(); i++) {
                    AstronomicalEvent astronomicalEvent = new AstronomicalEvent(array.getJSONObject(i));


                    if(astronomicalEvent.getMonth().equals(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US))
                            && astronomicalEvent.getYear().equals(calendar.get(Calendar.YEAR)+"")){
                        events.add(astronomicalEvent);
                    }
                }
            }

        } catch (Exception e) {
            Log.e("Json error", e.toString());
        }

        setListAdapter(new EventAdapter(this, android.R.layout.simple_list_item_1, events));


    }


    public void onListItemClick(ListView parent, View v, int position, long id) {
        Intent i = new Intent(this, EventDetails.class);
        AstronomicalEvent event = events.get(position);

        String[] eventText = {event.getEvent_name(), event.getDay_of_week(),
                event.getMonth(), event.getDay(), event.getYear(), event.getTime()};
        i.putExtra(DETAIL_NAME_STRING, eventText);

        String dateString;
        dateString = event.getYear() + "/" + event.getMonth() + "/" + event.getDay() + " " + event.getTime();
        i.putExtra(DETAIL_DATE_STRING, dateString);

        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_astro_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
