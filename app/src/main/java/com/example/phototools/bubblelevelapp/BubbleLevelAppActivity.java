package com.example.phototools.bubblelevelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phototools.R;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BubbleLevelAppActivity extends AppCompatActivity {

    TextView angleTextView;
    ImageView innerImageView;
    public static float angle = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_level_app);

        Objects.requireNonNull(getSupportActionBar()).show();
        getSupportActionBar().setTitle(R.string.waterMeterAppTextView);

        angleTextView = findViewById(R.id.angleTextView);
        innerImageView = findViewById(R.id.innerImageView);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> list = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] values = sensorEvent.values;
                angle = values[1] / 0.111111f;
                angleTextView.setText(String.format(Locale.US, "%.2f°", angle));

                innerImageView.animate().rotation(-angle).start();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };

        sensorManager.registerListener(sensorEventListener, list.get(0), SensorManager.SENSOR_DELAY_UI);
    }
}