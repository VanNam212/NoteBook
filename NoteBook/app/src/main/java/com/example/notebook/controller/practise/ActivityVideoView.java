package com.example.notebook.controller.practise;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.R;

public class ActivityVideoView extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupWindowAnimations();

        videoView = (VideoView) findViewById(R.id.videoView);

        Intent intent = getIntent();
        String name_movie = intent.getStringExtra("name_movie");

        // Set the media controller buttons
        if (this.mediaController == null) {
            this.mediaController = new MediaController(ActivityVideoView.this);

            // Set the videoView that acts as the anchor for the MediaController.
            this.mediaController.setAnchorView(videoView);

            // Set MediaController for VideoView
            this.videoView.setMediaController(mediaController);
        }

        // When the video file ready for playback.
        this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                videoView.seekTo(position);
                if (position == 0) {
                    videoView.start();
                }

                // When video Screen change size.
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        // Re-Set the videoView that acts as the anchor for the MediaController
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });

        try {
            // ID of video file.
            String pkgName = this.getPackageName();
            // Return 0 if not found.
            int id = this.getResources().getIdentifier(name_movie, "raw", pkgName);

            Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + id);

            videoView.setVideoURI(uri);
            videoView.requestFocus();

        } catch (Exception e) {
            Toast.makeText(ActivityVideoView.this, "Error Play Raw Video: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    // When you change direction of phone, this method will be called.
    // It store the state of video (Current position)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());
        videoView.pause();
    }

    // After rotating the phone. This method is called.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition");
        videoView.seekTo(position);
    }

    // Find ID corresponding to the name of the resource (in the directory RAW).
//    public static int getRawResIdByName() {
//        String pkgName = ActivityVideoView.this.getPackageName();
//        // Return 0 if not found.
//        int resID = context.getResources().getIdentifier(resName, "raw", pkgName);
//
////        Log.i(LOG_TAG, "Res Name: " + resName + "==> Res ID = " + resID);
//        return resID;
//    }

    private void setupWindowAnimations() {
        Explode explode1 = new Explode();
        explode1.setDuration(1000);
        getWindow().setExitTransition(explode1);

        Explode explode = new Explode();
        explode.setDuration(1000);
        getWindow().setEnterTransition(explode);

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setReturnTransition(fade);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
