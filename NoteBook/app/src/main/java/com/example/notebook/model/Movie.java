package com.example.notebook.model;

public class Movie {
    private int id;
    private int image;
    private String name_file;
    private String name_film;
    private String time;

    public Movie() {
    }

    public Movie(int id, int image, String name_file, String name_film, String time) {
        this.id = id;
        this.image = image;
        this.name_file = name_file;
        this.name_film = name_film;
        this.time = time;
    }

    public Movie(int image, String name_file, String name_film, String time) {
        this.image = image;
        this.name_file = name_file;
        this.name_film = name_film;
        this.time = time;
    }

    public String getName_file() {
        return name_file;
    }

    public void setName_file(String name_file) {
        this.name_file = name_file;
    }

    public String getName_film() {
        return name_film;
    }

    public void setName_film(String name_film) {
        this.name_film = name_film;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
