package com.example.phototools.lightpollutionapp;

import java.util.Calendar;

public class HourLightData {
    private String hour;
    private int sunLight;
    private int moonLight;

    public HourLightData(String hour, int sunLight, int moonLight) {
        this.hour = hour;
        this.sunLight = sunLight;
        this.moonLight = moonLight;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getSunLight() {
        return sunLight;
    }

    public void setSunLight(int sunLight) {
        this.sunLight = sunLight;
    }

    public int getMoonLight() {
        return moonLight;
    }

    public void setMoonLight(int moonLight) {
        this.moonLight = moonLight;
    }
}
