package com.example.shika.movies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.shika.movies.utils.Tages;

/**
 * Created by shika on 12/8/2015.
 */
public class MoviesDbDatasource {

    MoviesDbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    public MoviesDbDatasource(Context context){
        dbHelper = new MoviesDbHelper(context);
    }

    public Cursor getAllMoviesData(){
        String[] column = {Tages.id , Tages.title , Tages.poster_path};
        sqLiteDatabase = dbHelper.getReadableDatabase();
        return sqLiteDatabase.query(MoviesDbHelper.tableName , column , null ,null ,null ,null ,null);
    }


    public void SaveMovie(int id , String name , String poster){
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Tages.id , id);
        values.put(Tages.title , name);
        values.put(Tages.poster_path , poster);
        sqLiteDatabase.insert(MoviesDbHelper.tableName , null , values);
    }
}
