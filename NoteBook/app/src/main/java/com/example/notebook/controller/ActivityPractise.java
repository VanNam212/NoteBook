package com.example.notebook.controller;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.R;
import com.example.notebook.controller.practise.ActivityListVideo;
import com.example.notebook.controller.practise.ActivityPractiseBox;
import com.example.notebook.controller.practise.ActivityPractiseChoose;
import com.example.notebook.controller.practise.ActivityPractiseSong;
import com.example.notebook.controller.practise.ActivityPractiseSpeak;
import com.example.notebook.service.DBServiceImpl;

public class ActivityPractise extends AppCompatActivity {

    private Button button_speak;
    private Button button_choose;
    //    private Button button_new;
    private Button button_box;
    private Button button_movie;
    private Button button_song;

//    ExpandableListView expandableListView;
//    ExpandableListAdapter expandableListAdapter;
//    List<String> expandableListTitle;
//    HashMap<String, List<String>> expandableListDetail;

    private static final int REQ_ACTIVITY_PRACTISEBOX = 1;
    private static final int REQ_ACTIVITY_PRACTISECHOOSE = 2;
    private static final int REQ_ACTIVITY_PRACTISEMOVIE = 3;
    private static final int REQ_ACTIVITY_PRACTISESONG = 4;
    private static final int REQ_ACTIVITY_PRACTISESPEAK = 5;

    private DBServiceImpl dbWord = new DBServiceImpl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise_v2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupWindowAnimations();

        //anh xa
        button_box = (Button) findViewById(R.id.btnBox);
        button_choose = (Button) findViewById(R.id.btnChoose);
        button_speak = (Button) findViewById(R.id.btnSpeak);
        button_movie = (Button) findViewById(R.id.btnMovie);
        button_song = (Button) findViewById(R.id.btnSong);

        button_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbWord.getWordsCount() > 0) {
                    Intent intent = new Intent(ActivityPractise.this, ActivityPractiseBox.class);
                    startActivityForResult(intent, REQ_ACTIVITY_PRACTISEBOX);
                } else {
                    Toast.makeText(ActivityPractise.this, "Bạn không có từ vựng để luyện tập!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbWord.getWordsCount() >= 4) {
                    Intent intent = new Intent(ActivityPractise.this, ActivityPractiseChoose.class);
                    startActivityForResult(intent, REQ_ACTIVITY_PRACTISECHOOSE);
                } else {
                    Toast.makeText(ActivityPractise.this, "Cần phải có 4 từ vựng trở nên!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbWord.getWordsCount() > 0) {
                    Intent intent = new Intent(ActivityPractise.this, ActivityPractiseSpeak.class);
                    startActivityForResult(intent, REQ_ACTIVITY_PRACTISESPEAK);
                } else {
                    Toast.makeText(ActivityPractise.this, "Bạn không có từ vựng để luyện tập!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPractise.this, ActivityListVideo.class);
                startActivityForResult(intent, REQ_ACTIVITY_PRACTISEMOVIE);
            }
        });

        button_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPractise.this, ActivityPractiseSong.class);
                startActivityForResult(intent, REQ_ACTIVITY_PRACTISESONG);
            }
        });
    }

    private void setupWindowAnimations() {
        Slide slide1 = new Slide();
        slide1.setDuration(1000);
        getWindow().setExitTransition(slide1);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setEnterTransition(slide);

        Explode explode = new Explode();
        explode.setDuration(1000);
        getWindow().setReturnTransition(explode);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(ActivityPractise.this, MainActivity.class);
            setResult(RESULT_OK);
            startActivity(intent);
//            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQ_ACTIVITY_PRACTISEBOX:
                if (resultCode == RESULT_OK) {

                }
                break;
            case REQ_ACTIVITY_PRACTISECHOOSE:
                if (resultCode == RESULT_OK) {

                }
                break;
            case REQ_ACTIVITY_PRACTISEMOVIE:
                if (resultCode == RESULT_OK) {

                }
                break;
            case REQ_ACTIVITY_PRACTISESONG:
                if (resultCode == RESULT_OK) {

                }
                break;
            case REQ_ACTIVITY_PRACTISESPEAK:
                if (resultCode == RESULT_OK) {

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
