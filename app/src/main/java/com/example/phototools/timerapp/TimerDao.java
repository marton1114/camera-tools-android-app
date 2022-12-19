package com.example.phototools.timerapp;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface TimerDao {

    @Insert
    void insertListItem(TimerItem ti);

    @Query("SELECT * FROM Timer")
    LiveData<List<TimerItem>> getAllItems();

    @Query("DELETE FROM Timer")
    public void clearDB();

    @Update
    public void updateListItem(TimerItem ti);

    @Delete
    public void deleteListItem(TimerItem ti);
}