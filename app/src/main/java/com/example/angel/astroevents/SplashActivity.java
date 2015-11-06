package com.example.angel.astroevents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class SplashActivity extends AppCompatActivity {

    VideoView mSplashVideo;
    Button mListButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Use found here:http://stackoverflow.com/questions/15675944/how-to-play-video-from-raw-folder-with-android-device
        mSplashVideo = (VideoView)findViewById(R.id.splash_video);
        String path = "android.resource://"+getPackageName()+"/" + R.raw.night_keep;
        Uri uri = Uri.parse(path);
        mSplashVideo.setVideoURI(uri);
        mSplashVideo.setMediaController(new MediaController(this));
        mSplashVideo.requestFocus();
        mSplashVideo.start();

        mListButton = (Button)findViewById(R.id.list_button);
        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, AstroEventsActivity.class);
                startActivity(intent);
            }
        });
    }




}
