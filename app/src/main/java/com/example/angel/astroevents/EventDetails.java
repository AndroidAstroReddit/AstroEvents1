package com.example.angel.astroevents;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDetails extends AppCompatActivity {

    TextView tv;
    Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);
        //TODO: not using this yet
       String text = getIntent().getStringExtra(AstroEventsActivity.DETAIL_NAME_STRING);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm");
        String dateString = getIntent().getStringExtra(AstroEventsActivity.DETAIL_DATE_STRING);
        Date date = null;
        try{
            Log.i("Date String", dateString);
            date = formatter.parse(dateString);
        }catch(ParseException pe){
            Log.e("Date Parsing", pe.toString());
        }

        String key = getKeyFromRawResource();
        String wuAutoipBase = "http://api.wunderground.com/api/%s/geolookup/q/autoip.json";
        String wuAutoipUrl = String.format(wuAutoipBase, key);
        new RequestCity().execute(wuAutoipUrl);

//TODO change this test text to one well actually use
        tv = (TextView)findViewById(R.id.test_text);
        if(date !=null) {
            tv.setText(date.toString());
        }

        postButton = (Button) findViewById(R.id.open_post_activity_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventDetails.this, EventPost.class);
                startActivity(i);
            }
        });

    }

    private String getKeyFromRawResource(){
        InputStream keyStream = getResources().openRawResource(R.raw.key);
        BufferedReader keyStreamReader = new BufferedReader(new InputStreamReader(keyStream));
        try{
            String key = keyStreamReader.readLine();
            return key;
        } catch (IOException e){
            Log.e("Error","Error reading secret key from raw resource file");
            return null;
        }
    }

    class RequestCity extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            String responseString = null;


            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream responseStream = new BufferedInputStream(connection.getInputStream());
                InputStreamReader streamReader = new InputStreamReader(responseStream);
                StringBuffer buffer = new StringBuffer();

                int c;
                while ((c = streamReader.read()) != -1) {
                    buffer.append((char) c);
                }

                responseString = buffer.toString();

                Log.e("WEATHER", "String is " + responseString);
            } catch (Exception e) {
                Log.e("Error", "Error fetching weather data, see exception for details: ", e);
            }
            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject response = new JSONObject(result);
                    JSONObject autoip = response.getJSONObject("location");
                    String city = autoip.getString("city");
                    String state = autoip.getString("state");
                    tv.setText(city + ", " + state);
                } catch (JSONException e) {
                    Log.e("Error", "parsing error, check schema?", e);
                    tv.setText("Error fetching city");
                }
            } else {
                tv.setText("Error fetching city");
                Log.e("Error", "Result was null, check doInBackground for errors");
            }
        }
    }
}

