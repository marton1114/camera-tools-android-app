package com.example.phototools.timerapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Timer")
public class TimerItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int itemId;

    String description;
    long date;

    boolean switchValue;

    public boolean isSwitchValue() {
        return switchValue;
    }

    public void setSwitchValue(boolean switchValue) {
        this.switchValue = switchValue;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return itemId + " - "+ description;
    }
}
