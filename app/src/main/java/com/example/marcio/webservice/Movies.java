package com.example.marcio.webservice;

import java.util.ArrayList;
import java.util.Arrays;

public class Movies
{

    private int id_filme;
    private String title;
    private String image;
    private float rating;
    private int releaseYear;
    private String []genre;

    @Override
    public String toString() {
        return "Movies{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", rating=" + rating +
                ", releaseYear=" + releaseYear +
                ", genre=" + Arrays.toString(genre) +
                '}';
    }

    public int getId_filme() {
        return id_filme;
    }

    public void setId_filme(int id_filme) {
        this.id_filme = id_filme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String[] getGenre() {
        return genre;
    }

    public void setGenre(String[] genre) {
        this.genre = genre;
    }
}
