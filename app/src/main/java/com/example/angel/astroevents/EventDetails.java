package com.example.angel.astroevents;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

        String key = getKeyFromRawResource();
        String wuAutoipBase = "http://api.wunderground.com/api/%s/geolookup/q/autoip.json";
        String wuAutoipUrl = String.format(wuAutoipBase, key);
        new RequestCity().execute(wuAutoipUrl);






        tv = (TextView)findViewById(R.id.test_text);
        if(date !=null) {
            tv.setText(date.toString());
        }
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

