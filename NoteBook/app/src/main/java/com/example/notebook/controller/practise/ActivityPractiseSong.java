package com.example.notebook.controller.practise;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.R;
import com.example.notebook.adapter.SongAdapter;
import com.example.notebook.model.Song;
import com.example.notebook.service.MusicService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityPractiseSong extends AppCompatActivity implements MediaController.MediaPlayerControl {

    private MusicController controller;
    private ArrayList<Song> songList;
    private ListView songView;
    private MusicService musicSrv;
    private Intent playIntent;
//    private EditText editText;
//    private ImageButton btnSearch;
    private boolean musicBound = false;
    private boolean paused = false, playbackPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise_song);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        songView = (ListView) findViewById(R.id.song_list);
//        editText = (EditText) findViewById(R.id.editText);
//        btnSearch = (ImageButton) findViewById(R.id.btnSearch);

        songList = new ArrayList<Song>();

        getSongList();
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
        setController();

//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (editText.getText().toString().trim() != "") {
//
//                }
//            }
//        });
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void setController() {
        //set the controller up
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.song_list));
        controller.setEnabled(true);
    }

    public void getSongList() {
        //retrieve song info
//        ContentResolver musicResolver = getContentResolver();
//        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
//        if (musicCursor != null && musicCursor.moveToFirst()) {
//            //get columns
//            int titleColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media.TITLE);
//            int idColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media._ID);
//            int artistColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media.ARTIST);
//            //add songs to list
//            do {
//                long thisId = musicCursor.getLong(idColumn);
//                String thisTitle = musicCursor.getString(titleColumn);
//                String thisArtist = musicCursor.getString(artistColumn);
//                songList.add(new Song(thisId, thisTitle, thisArtist));
//            }
//            while (musicCursor.moveToNext());
//        }

        songList.add(new Song(0,"A little love",R.raw.a_little_love_fionafung,"Fiona Fung"));
        songList.add(new Song(1,"Animals",R.raw.animals_maroon5,"Maroon"));
        songList.add(new Song(2,"Attention",R.raw.attention_charlie_puth,"Charlie Puth"));
        songList.add(new Song(3,"Beautiful in white",R.raw.beautiful_in_white_shanefilan,"Shane Filan"));
        songList.add(new Song(4,"Can't stop the feeling",R.raw.cant_stop_the_feeling_justintimberl,"Justin Timberl"));
        songList.add(new Song(5,"Cheap thrills",R.raw.cheap_thrills_sia,"Sia"));
        songList.add(new Song(6,"Don't let me down",R.raw.dont_let_me_down_thechainsmokers,"The Chainsmokers"));
        songList.add(new Song(7,"Feel me",R.raw.feel_me_selenagomez,"Selena Gomez"));
        songList.add(new Song(8,"I do",R.raw.i_do_lyric,"Lyric"));
        songList.add(new Song(9,"I hate you i love you",R.raw.i_hate_you_i_love_you_gnasholivia,"Gnash Olivia"));
        songList.add(new Song(10,"If i were a boy",R.raw.if_i_were_a_boy_beyonce,"Beyonce"));
        songList.add(new Song(11,"Oah",R.raw.oah_alexanderrybak,"Alexander Rybak"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    public void songPicked(View view) {
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        String x = musicSrv.playSong();
        setTitle(x);
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            setResult(RESULT_OK);
//            finish();
//            return true;
//        }
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                return true;
            case R.id.action_shuffle:
                //shuffle
                musicSrv.setShuffle();
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv = null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if (musicSrv != null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    //play next
    private void playNext() {
        musicSrv.playNext();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    //play previous
    private void playPrev() {
        musicSrv.playPrev();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            setController();
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }
}
