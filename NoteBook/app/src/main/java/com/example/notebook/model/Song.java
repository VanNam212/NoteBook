package com.example.notebook.model;

public class Song {
    private long id;
    private String title;
    private int name_file;
    private String artist;

    public Song(long songID, String songTitle, String songArtist) {
        id = songID;
        title = songTitle;
        artist = songArtist;
    }

    public Song(long id, String title, int name_file, String artist) {
        this.id = id;
        this.title = title;
        this.name_file = name_file;
        this.artist = artist;
    }

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public Song(String title, int name_file, String artist) {
        this.title = title;
        this.name_file = name_file;
        this.artist = artist;
    }

    public int getName_file() {
        return name_file;
    }

    public void setName_file(int name_file) {
        this.name_file = name_file;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
