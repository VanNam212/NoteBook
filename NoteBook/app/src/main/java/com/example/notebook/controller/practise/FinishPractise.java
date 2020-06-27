package com.example.notebook.controller.practise;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.R;
import com.example.notebook.controller.ActivityPractise;

public class FinishPractise extends AppCompatActivity {

    private Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_practise);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupWindowAnimations();

        btnFinish = (Button) findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishPractise.this, ActivityPractise.class);
                startActivity(intent);
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
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
