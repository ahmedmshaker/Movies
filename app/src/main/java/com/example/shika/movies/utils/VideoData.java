package com.example.shika.movies.utils;

/**
 * Created by shika on 12/22/2015.
 */
public class VideoData {

    public String getKey() {
        return Key;
    }

    public String getName() {
        return Name;
    }

    String Key;
    String Name;

    public VideoData(String key , String name){
        this.Key = key;
        this.Name = name;
    }
}
