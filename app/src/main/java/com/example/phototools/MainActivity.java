package com.example.phototools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.phototools.bubblelevelapp.BubbleLevelAppActivity;
import com.example.phototools.exposureapp.activity_exposure;
import com.example.phototools.lightpollutionapp.LightPollutionAppActivity;
import com.example.phototools.lightsapp.LightsAppActivity;
import com.example.phototools.starmovingapp.StarMovingAppActivity;
import com.example.phototools.startrailsapp.StarTrailsAppActivity;
import com.example.phototools.sunandmoonapp.SunAndMoonAppActivity;
import com.example.phototools.timelapseapp.TimeLapseAppActivity;
import com.example.phototools.timerapp.TimerAppActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public void onSunAndMoonAppButtonClick(View view) {
        Intent intent = new Intent(this, SunAndMoonAppActivity.class);
        startActivity(intent);
    }

    public void onExposureAppButtonClick(View view) {
        Intent intent = new Intent(this, activity_exposure.class);
        startActivity(intent);
    }

    public void onBubbleLevelAppButtonClick(View view) {
        Intent intent = new Intent(this, BubbleLevelAppActivity.class);
        startActivity(intent);
    }

    public void onTimerAppButtonClick(View view) {
        Intent intent = new Intent(this, TimerAppActivity.class);
        startActivity(intent);
    }

    public void onTimeLapseAppButtonClick(View view) {
        Intent intent = new Intent(this, TimeLapseAppActivity.class);
        startActivity(intent);
    }

    public void onLightsAppButtonClick(View view) {
        Intent intent = new Intent(this, LightsAppActivity.class);
        startActivity(intent);
    }

    public void onStarMovingAppButtonClick(View view) {
        Intent intent = new Intent(this, StarMovingAppActivity.class);
        startActivity(intent);
    }

    public void onLightPollutionAppButtonClick(View view) {
        Intent intent = new Intent(this, LightPollutionAppActivity.class);
        startActivity(intent);
    }

    public void onStarTrailsAppButtonClick(View view) {
        Intent intent = new Intent(this, StarTrailsAppActivity.class);
        startActivity(intent);
    }
}