package com.example.vaibhav.myfavmovie;


import android.support.annotation.NonNull;

import java.io.Serializable;

public class Movie implements  Comparable,Serializable{
    public  static int movieCount = 0;
    String name;
    String description;
    String genre;
    int ratig;
    int year;
    String imdbRating;
    int movieID;

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID() {
        this.movieID = movieCount++;
    }


    public  Movie()
    {}
    public Movie(String name, String description, String genre, int ratig, int year, String imdbRating) {
        this.setDescription(description);
        this.setGenre(genre);
        this.setImdbRating(imdbRating);
        this.setName(name);
        this.setRatig(ratig);
        this.setYear(year);
        this.setMovieID();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getRatig() {
        return ratig;
    }

    public void setRatig(int ratig) {
        this.ratig = ratig;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImdbRating() {
        return imdbRating;
    }



    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }
    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", ratig=" + ratig +
                ", year=" + year +
                ", imdbRating='" + imdbRating + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Object movie) {
        int comapreYear =((Movie)movie).getYear();
        return this.getYear()- comapreYear;
    }
}
