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

public class EventDetails extends AppCompatActivity {

    TextView details;
    TextView temperature;
    TextView cloudCover;
    TextView state_and_city;
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

        details = (TextView) findViewById(R.id.event_details);

        temperature = (TextView)findViewById(R.id.temperature);
        cloudCover = (TextView)findViewById(R.id.cloud_cover);
        wind = (TextView)findViewById(R.id.wind);
        state_and_city = (TextView)findViewById(R.id.state_and_city);


        String key = getKeyFromRawResource();
        String wuAutoipBase = "http://api.wunderground.com/api/%s/geolookup/q/autoip.json";
        String wuAutoipUrl = String.format(wuAutoipBase, key);
        new RequestCity().execute(wuAutoipUrl);

        details = (TextView)findViewById(R.id.event_details);
        temperature = (TextView)findViewById(R.id.temperature);
        cloudCover = (TextView)findViewById(R.id.cloud_cover);
        wind = (TextView)findViewById(R.id.wind);






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
                    String requestUrl = autoip.getString("requesturl");
                    state_and_city.setText("State: " + state + " City: "+ city);
                    String key = getKeyFromRawResource();
                    String BaseUrl = "http://api.wunderground.com/api/%s/forecast10day/q/" + requestUrl + ".json";
                    String forecastUrl = String.format(BaseUrl, key);
                    new RequestForecast().execute(forecastUrl);
                } catch (JSONException e) {
                    Log.e("Error parsing city", e.toString());
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
                Log.i("Forecast", responseString);

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

                    for (int i = 0; i < forecast.length(); i++){
                        JSONObject dayDetails = forecast.getJSONObject(i);

                        if (Integer.parseInt(event.getDay()) == Integer.parseInt(dayDetails.getJSONObject("date").getString("day"))
                                && event.getMonth().equals(dayDetails.getJSONObject("date").getString("monthname_short"))
                                && Integer.parseInt(event.getYear()) == Integer.parseInt(dayDetails.getJSONObject("date").getString("year"))) {
                            details.setText("Forecast for the " + event.getEvent_name() + " event on " + event.getDay_of_week() + ", " +
                            event.getMonth() + " " + event.getDay()  + ":");
                            temperature.setText("High: " + dayDetails
                            .getJSONObject("high").getString("fahrenheit") + "F, Low: " + dayDetails
                                    .getJSONObject("low").getString("fahrenheit") + "F");
                            cloudCover.setText(dayDetails.getString("conditions") +" (" + dayDetails.getString("pop") + "% chance of precipitation, "
                                    + dayDetails.getJSONObject("qpf_allday").getString("in") + " inches of estimated precipitation)");
                            wind.setText(dayDetails.getJSONObject("avewind").getString("mph") + " mph wind with gusts of "
                                    + dayDetails.getJSONObject("maxwind").getString("mph"));

                            break;
                        }
                    }

                } catch (JSONException e) {
                    Log.e("Error", "parsing error, check schema?", e);
                    temperature.setText("Error fetching forecast json exception");
                }
            } else {
                temperature.setText("Error fetching forecast");
                Log.e("Error", "Result was null, check doInBackground for errors");
            }
        }
    }

}

