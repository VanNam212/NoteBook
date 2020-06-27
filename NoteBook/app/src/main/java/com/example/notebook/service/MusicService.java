package com.example.notebook.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;

import com.example.notebook.R;
import com.example.notebook.controller.practise.ActivityPractiseSong;
import com.example.notebook.model.Song;

import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    ArrayList<Song> songList;
    public void getSong(){
        songList.add(new Song(110,"A little love",R.raw.a_little_love_fionafung,"Fiona Fung"));
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

    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;

    private final IBinder musicBind = new MusicBinder();

    private String songTitle = "";
    private static final int NOTIFY_ID = 1;

    private boolean shuffle = false;
    private Random rand;

    public void onCreate() {
        //create the service
        //create the service
        super.onCreate();
        //initialize position
        songPosn = 0;
        //create player
        player = new MediaPlayer();
        rand = new Random();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> theSongs) {
        songs = theSongs;
    }

    public String playSong() {
        //play a song
        player.reset();
        songList = new ArrayList<Song>();
        getSong();
        //get song
        Song playSong = songs.get(songPosn);
        songTitle = playSong.getTitle();
        //get id
        long currSong = playSong.getID();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        player = MediaPlayer.create(getApplicationContext(), songList.get(Integer.parseInt(currSong+"")).getName_file());
        return  ""+songList.get(Integer.parseInt(currSong+"")).getTitle();
//        try {
////            player.setDataSource(getApplicationContext(), trackUri);
//            String x = ""+playSong.getName_file();
//            player.setDataSource(getApplicationContext(), Uri.parse(x));
//        } catch (Exception e) {
//            Log.e("MUSIC SERVICE", "Error setting data source", e);
//            Toast.makeText(this, "loi 1", Toast.LENGTH_SHORT).show();
//        }
//        try {
//            player.prepareAsync();
//        } catch (IllegalStateException e) {
//            Log.e("music", "error", e);
//            Toast.makeText(this, "loi 2", Toast.LENGTH_SHORT).show();
//        }
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
        Intent notIntent = new Intent(this, ActivityPractiseSong.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public int getPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
        player.start();
    }

    public void playPrev() {
        songPosn--;
        if (songPosn < 0) songPosn = songs.size() - 1;
        playSong();
    }

    //skip to next
    public void playNext() {
        if (shuffle) {
            int newSong = songPosn;
            while (newSong == songPosn) {
                newSong = rand.nextInt(songs.size());
            }
            songPosn = newSong;
        } else {
            songPosn++;
            if (songPosn == songs.size()) songPosn = 0;
        }
        playSong();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void setShuffle() {
        if (shuffle) shuffle = false;
        else shuffle = true;
    }
}
