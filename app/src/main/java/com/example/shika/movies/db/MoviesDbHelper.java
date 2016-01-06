package com.example.shika.movies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shika.movies.utils.Tages;

/**
 * Created by shika on 12/8/2015.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {
    private static final String DatabaseName = "Movies.db";
    public static final String tableName = "Movies";

    public MoviesDbHelper(Context context) {
        super(context, DatabaseName, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+tableName+
        "("+ Tages.id +" Integer primary key ," +
               Tages.title +" text ,"+Tages.poster_path+" text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+tableName);
    }
}
