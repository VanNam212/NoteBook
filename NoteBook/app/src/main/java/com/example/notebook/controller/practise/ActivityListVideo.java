package com.example.notebook.controller.practise;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.R;
import com.example.notebook.adapter.MovieAdapter;
import com.example.notebook.controller.ActivityPractise;
import com.example.notebook.model.Movie;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.ArrayList;

public class ActivityListVideo extends AppCompatActivity {

    private EditText editText;
    private ImageButton btnSearch;
    private ListView movie_list;
    private ArrayList<Movie> movies = new ArrayList<>();
    MovieAdapter movieAdapter;

    private static final int REQ_VIDEO_VIEW = 1;
    private static final int REQ_WEB_VIEW = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText = (EditText) findViewById(R.id.editText);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        movie_list = (ListView) findViewById(R.id.movie_list);

        findViewById(R.id.constraint).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                UIUtil.hideKeyboard(ActivityListVideo.this);
                return true;
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen) {
                    Toast.makeText(ActivityListVideo.this, "keyboard opened", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityListVideo.this, "keyboard closed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        addListMovie();
        movieAdapter = new MovieAdapter(this, movies);
        movie_list.setAdapter(movieAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtil.hideKeyboard(ActivityListVideo.this);
                int check = 0;
                if (editText.getText().toString().trim() != "") {
                    movieAdapter.getFilter().filter(editText.getText().toString().trim());
                    if (movieAdapter.getCount() == 0) {
                        movieAdapter.getFilter().filter("");
                        editText.setText("");

                    } else{
                        check = 1;
                    }

                }
                if (check == 0) {
                    Intent intent = new Intent(ActivityListVideo.this, ActivityWebView.class);

                    startActivityForResult(intent, REQ_WEB_VIEW);
                }
            }
        });

        movie_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                Intent intent = new Intent(ActivityListVideo.this, ActivityVideoView.class);
                intent.putExtra("name_movie", movie.getName_file());
                startActivityForResult(intent, REQ_VIDEO_VIEW);
            }
        });
    }

    private void addListMovie() {
        movies.add(new Movie(R.drawable.harry1, "harry_potter1", "Harry Potter 1", "00:02:47"));
        movies.add(new Movie(R.drawable.harry_potter_1, "harry_potter2", "Harry Potter 2", "00:03:27"));
        movies.add(new Movie(R.drawable.harry_potter_2, "harry_potter3", "Harry Potter 3", "00:04:30"));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_VIDEO_VIEW && resultCode == RESULT_OK) {
//            movie_list.setAdapter();
        } else if (requestCode == REQ_WEB_VIEW && resultCode == RESULT_OK) {
            editText.setText("");
            movie_list.setAdapter(movieAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(ActivityListVideo.this, ActivityPractise.class);
            startActivity(intent);
//            setResult(RESULT_OK);
//            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
