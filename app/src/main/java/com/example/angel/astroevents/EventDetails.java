package com.example.angel.astroevents;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);
        String text = getIntent().getStringExtra(AstroEventsActivity.DETAIL_NAME_STRING);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm");
        String date_string = getIntent().getStringExtra(AstroEventsActivity.DETAIL_DATE_STRING);
        Date date = null;
        try{
            Log.i("Date String", date_string);
            date = formatter.parse(date_string);
        }catch(ParseException pe){
            Log.e("Date Parsing", pe.toString());
        }


        TextView tv = (TextView)findViewById(R.id.test_text);
        if(date !=null) {
            tv.setText(date.toString());
        }
    }
}
