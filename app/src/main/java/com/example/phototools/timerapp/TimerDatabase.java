package com.example.phototools.timerapp;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TimerItem.class}, version = 4, exportSchema = false)
public abstract class TimerDatabase extends RoomDatabase {
    public abstract TimerDao timerDao();
}
