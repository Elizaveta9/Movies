package com.example.movies.database;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.movies.pojo.Movie;

@androidx.room.Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static final String DB_NAME = "movies.db";

    private static Database instance = null;

    public static Database getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(application, Database.class, DB_NAME).build();
        }

        return instance;
    }

    public abstract MovieDao movieDao();
}
