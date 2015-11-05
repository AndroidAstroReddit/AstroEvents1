package com.example.angel.astroevents;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
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
import java.util.GregorianCalendar;

public class EventDetails extends AppCompatActivity {

    TextView temperature;
    TextView cloudCover;

    TextView wind;
    Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

       //String[] eventDetails = getIntent().getStringArrayExtra(AstroEventsActivity.DETAIL_NAME_STRING);
       // AstronomicalEvent event = new AstronomicalEvent(eventDetails[0], eventDetails[1], eventDetails[2], eventDetails[3],
        //eventDetails[4], eventDetails[5]);




        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm");
        String dateString = getIntent().getStringExtra(AstroEventsActivity.DETAIL_DATE_STRING);
        Date date = null;
        try{
            Log.i("Date String", dateString);
            date = formatter.parse(dateString);
        }catch(ParseException pe){
            Log.e("Date Parsing", pe.toString());
        }

        temperature = (TextView)findViewById(R.id.temperature);
        cloudCover = (TextView)findViewById(R.id.cloud_cover);
        wind = (TextView)findViewById(R.id.wind);


        String key = getKeyFromRawResource();
        String wuAutoipBase = "http://api.wunderground.com/api/%s/geolookup/q/autoip.json";
        String wuAutoipUrl = String.format(wuAutoipBase, key);
        new RequestCity().execute(wuAutoipUrl);







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
                    temperature.setText("State: " + state + " City: "+ city);
                    String key = getKeyFromRawResource();
                    String BaseUrl = "http://api.wunderground.com/api/%s/forecast10day/q/" + state +"/" + city + ".json";
                    String forecastUrl = String.format(BaseUrl, key);
                    new RequestForecast().execute(forecastUrl);
                } catch (JSONException e) {
                    Log.e("Error", "parsing error, check schema?", e);
                    //tv.setText("Error fetching city");
                }
            } else {
                //tv.setText("Error fetching city");
                Log.e("Error", "Result was null, check doInBackground for errors");
            }
        }
    }

    class RequestForecast extends AsyncTask<String, String, String>{
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
                Log.i("Forecast: ", responseString);

            } catch (Exception e) {
                Log.e("Error", "Error fetching weather data, see exception for details: ", e);
            }
            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject response = new JSONObject(result).getJSONObject("forecast").getJSONObject("simpleforecast");
                    JSONArray forecast = response.getJSONArray("forecastday");

                    String[] eventDetails = getIntent().getStringArrayExtra(AstroEventsActivity.DETAIL_NAME_STRING);
                    AstronomicalEvent event = new AstronomicalEvent(eventDetails[0], eventDetails[1], eventDetails[2], eventDetails[3].replaceAll("\\s", ""),
                            eventDetails[4].replaceAll("\\s", ""), eventDetails[5]);

                    for (int i = 0; i < forecast.length(); i++) {
                        if (Integer.parseInt(event.getDay()) == Integer.parseInt(forecast.getJSONObject(i).getJSONObject("date").getString("day"))
                                && event.getMonth().equals(forecast.getJSONObject(i).getJSONObject("date").getString("monthname_short"))
                                && Integer.parseInt(event.getYear()) == Integer.parseInt(forecast.getJSONObject(i).getJSONObject("date").getString("year"))) {
                            temperature.setText("The high temp for the event will be " + forecast.getJSONObject(i)
                            .getJSONObject("high").getString("fahrenheit") + "F, and the low will be " + forecast.getJSONObject(i)
                                    .getJSONObject("low").getString("fahrenheit") + "F");
                            cloudCover.setText(forecast.getJSONObject(i).getString("conditions"));
                            wind.setText(forecast.getJSONObject(i).getJSONObject("avewind").getString("mph") + " mph wind with gusts of "
                            + forecast.getJSONObject(i).getJSONObject("maxwind").getString("mph"));

                            break;
                        }
                    }

                } catch (JSONException e) {
                    Log.e("Error", "parsing error, check schema?", e);
                    temperature.setText("Error fetching city json exception");
                }
            } else {
                temperature.setText("Error fetching city");
                Log.e("Error", "Result was null, check doInBackground for errors");
            }
        }
    }

}

