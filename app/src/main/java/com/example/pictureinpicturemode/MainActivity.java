package com.example.pictureinpicturemode;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PictureInPictureParams;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.view.View;

import com.example.pictureinpicturemode.databinding.ActivityMainBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding activityMainBinding;
    ExoPlayer videoStreamingView;
    private DataSource.Factory mediaDataSourceFactory;
    String URL = "https://cdn.anydone.com/04aa8811191e478d8f8dcfc3709548a2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        initializeBtn();
        initializePlayer();

    }

    private void initializeBtn() {
        activityMainBinding.btnPIPmode.setOnClickListener(this);
    }


    private void initializePlayer() {
        mediaDataSourceFactory =new DefaultDataSource.Factory(MainActivity.this);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(MediaItem.fromUri(URL));
        MediaSource.Factory mediaSourceFactory = new DefaultMediaSourceFactory(mediaDataSourceFactory);

        videoStreamingView =new ExoPlayer.Builder(this).setMediaSourceFactory(mediaSourceFactory).build();
        videoStreamingView.addMediaSource(mediaSource);
        videoStreamingView.setPlayWhenReady(true);
        activityMainBinding.imgFullScreenImageSection.setPlayer(videoStreamingView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void pictureInPictureMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Rational aspectRatio = new Rational(16, 29);
            PictureInPictureParams params = new PictureInPictureParams.Builder()
                    .setAspectRatio(aspectRatio)
                    .build();
            enterPictureInPictureMode(params);
        }
    }

    @Override
    public void onStart() {
        if (Util.SDK_INT > 23) initializePlayer();
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPause() {
        super.onPause();
        if (isInPictureInPictureMode()) {
            videoStreamingView.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoStreamingView.setPlayWhenReady(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoStreamingView.setPlayWhenReady(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPIPmode:
                pictureInPictureMode();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        pictureInPictureMode();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBackPressed(){
        if (isInPictureInPictureMode()){
            moveTaskToBack(true);
        }
        else {
            pictureInPictureMode();
        }
    }

}