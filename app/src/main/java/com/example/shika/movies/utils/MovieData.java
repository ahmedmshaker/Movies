package com.example.shika.movies.utils;

/**
 * Created by shika on 12/18/2015.
 */
public class MovieData {


    public String getTitle() {
        return Title;
    }

    public int getId() {
        return id;
    }

    public String getPosterImage() {
        return PosterImage;
    }



    int id;
    String Title;
    String PosterImage;


    public MovieData(int id , String title , String poster){
        this.id = id;
        this.Title = title;
        this.PosterImage = poster;

    }
}
