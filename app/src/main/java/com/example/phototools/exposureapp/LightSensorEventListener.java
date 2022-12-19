package com.example.phototools.exposureapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import com.example.phototools.R;

public class LightSensorEventListener implements SensorEventListener {
    private TextView tv;
    private float shutter;
    private float aperture;
    private int iso;

    public void setShutter(float shutter) {
        this.shutter = shutter;
    }

    public void setAperture(float aperture) {
        this.aperture = aperture;
    }

    public void setIso(int iso) {
        this.iso = iso;
    }

    public void setTv(TextView tv) {
        this.tv = tv;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float lux = sensorEvent.values[0];

        if (tv.getId() == R.id.shutterTextView) {
            tv.setText(CameraUtils.closestShutter(CameraUtils.calculateShutter(lux, aperture, iso)));
            // System.out.println("ev: " + CameraUtils.luxToEv(lux));
            // System.out.println("lux: " + lux);
            // System.out.println("max lux: " + MainActivity.getmLight().getMaximumRange());
        } else if (tv.getId() == R.id.isoTextView) {
            tv.setText(CameraUtils.closestIso(CameraUtils.calculateIso(lux, shutter, aperture)));
        } else if (tv.getId() == R.id.apertureTextView) {
            tv.setText(CameraUtils.closestAperture(CameraUtils.calculateAperture(lux, shutter, iso)));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //TODO
    }
}