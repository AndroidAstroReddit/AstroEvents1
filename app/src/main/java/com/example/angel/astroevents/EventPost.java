package com.example.angel.astroevents;


import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EventPost extends AppCompatActivity {
    Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_post);
        postButton = (Button) findViewById(R.id.post_button);

        postButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              new postToTwitter().execute();

                                          }
                                      }
            );


    }

    class postToTwitter extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            String responseString = "Success";
            String[] tokens = getKeysandTokens();
            String consumerKeyStr = tokens[0];
            String consumerSecretStr = tokens[1];
            String accessTokenStr = tokens[2];
            String accessTokenSecretStr = tokens[3];

            try {
                Twitter twitter = new TwitterFactory().getInstance();

                twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
                AccessToken accessToken = new AccessToken(accessTokenStr,
                        accessTokenSecretStr);

                twitter.setOAuthAccessToken(accessToken);

                twitter.updateStatus("test");

                System.out.println("Successfully updated the status in Twitter.");
            } catch (TwitterException te) {
                te.printStackTrace();
            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                    Log.i("Success", "result");
            } else {
                //tv.setText("Error fetching city");
                Log.e("Error", "Result was null, check doInBackground for errors");
            }
        }
    }


    private String[] getKeysandTokens(){
        String[] keys = new String[4];
        InputStream keyStream = getResources().openRawResource(R.raw.tokens);
        BufferedReader keyStreamReader = new BufferedReader(new InputStreamReader(keyStream));
        try{
            int i=0;
            while(i<4) {
                keys[i] = keyStreamReader.readLine();
                i++;
            }
            return keys;
        } catch (IOException e){
            Log.e("Error","Error reading secret key from raw resource file");
            return null;
        }
    }
}


