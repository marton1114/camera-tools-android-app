package com.example.phototools.exposureapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phototools.R;


public class activity_exposure extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;

    Button shutterLeftButton;
    Button shutterRightButton;
    Button isoLeftButton;
    Button isoRightButton;
    Button apertureLeftButton;
    Button apertureRightButton;

    TextView shutterTextView;
    TextView isoTextView;
    TextView apertureTextView;

    TextView textViewToCalculate;

    Button measureButton;

    private SensorManager sensorManager;
    private LightSensorEventListener sel = new LightSensorEventListener();
    private static Sensor mLight;

    int shutterIndex = 8;
    int isoIndex = 0;
    int apertureIndex = 4;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().show();
        getSupportActionBar().setTitle(R.string.exposureAppTextView);

        setContentView(R.layout.activity_exposure);

        radioGroup = findViewById(R.id.radioGroup);

        shutterTextView = findViewById(R.id.shutterTextView);
        isoTextView = findViewById(R.id.isoTextView);
        apertureTextView = findViewById(R.id.apertureTextView);

        shutterLeftButton = findViewById(R.id.shutterLeftButton);
        shutterRightButton = findViewById(R.id.shutterRightButton);
        isoLeftButton = findViewById(R.id.isoLeftButton);
        isoRightButton = findViewById(R.id.isoRightButton);
        apertureLeftButton = findViewById(R.id.apertureLeftButton);
        apertureRightButton = findViewById(R.id.apertureRightButton);

        measureButton = findViewById(R.id.measureButton);

        textViewToCalculate = shutterTextView;

        PackageManager manager = getPackageManager();
        boolean hasLightSensor = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);
        if (! hasLightSensor) {
            Toast.makeText(this, R.string.noSensorMessage, Toast.LENGTH_LONG);
        }

        sensorManager = (SensorManager)  getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        setStateOfTextViews(shutterIndex, isoIndex, apertureIndex);

        measureButton.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                sensorManager.registerListener(sel, mLight, SensorManager.SENSOR_DELAY_NORMAL);

                sel.setTv(textViewToCalculate);
                sel.setShutter(CameraUtils.shutterStringToFloat(CameraUtils.getShutters().get(shutterIndex)));
                sel.setIso(Integer.parseInt(CameraUtils.getIsos().get(isoIndex)));
                sel.setAperture(Float.parseFloat(CameraUtils.getApertures().get(apertureIndex)));

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                sensorManager.unregisterListener(sel);
            }
            return false;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setStateOfTextViews(int newShutterIndex, int newIsoIndex, int newApertureIndex) {
        if (newShutterIndex <= CameraUtils.getShutters().size() - 1 && newShutterIndex >= 0)
            this.shutterIndex = newShutterIndex;
        if (newIsoIndex <= CameraUtils.getIsos().size() - 1 && newIsoIndex >= 0)
            this.isoIndex = newIsoIndex;
        if (newApertureIndex <= CameraUtils.getApertures().size() - 1 && newApertureIndex >= 0)
            this.apertureIndex = newApertureIndex;

        if (textViewToCalculate != shutterTextView)
            shutterTextView.setText(CameraUtils.getShutters().get(shutterIndex));
        if (textViewToCalculate != isoTextView)
            isoTextView.setText(CameraUtils.getIsos().get(isoIndex));
        if (textViewToCalculate != apertureTextView)
            apertureTextView.setText(CameraUtils.getApertures().get(apertureIndex));
    }

    private void setStateOfButtons(Boolean bp1, Boolean bp2, Boolean bp3) {
        shutterLeftButton.setEnabled(bp1);
        shutterRightButton.setEnabled(bp1);
        isoLeftButton.setEnabled(bp2);
        isoRightButton.setEnabled(bp2);
        apertureLeftButton.setEnabled(bp3);
        apertureRightButton.setEnabled(bp3);
    }

    public void onRadioButtonClick(View view) {
        int checkedId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(checkedId);

        if (checkedId == R.id.shutterRadioButton) {
            setStateOfButtons(false, true, true);
            textViewToCalculate = shutterTextView;
        } else if (checkedId == R.id.isoRadioButton) {
            setStateOfButtons(true, false, true);
            textViewToCalculate = isoTextView;
        } else if (checkedId == R.id.apertureRadioButton) {
            setStateOfButtons(true, true, false);
            textViewToCalculate = apertureTextView;
        }

        setStateOfTextViews(shutterIndex, isoIndex, apertureIndex);
    }

    public void onNextButtonClick(View view) {
        if (view.getId() == shutterRightButton.getId())
            setStateOfTextViews(shutterIndex + 1, isoIndex, apertureIndex);
        else if (view.getId() == isoRightButton.getId())
            setStateOfTextViews(shutterIndex, isoIndex + 1, apertureIndex);
        else
            setStateOfTextViews(shutterIndex, isoIndex, apertureIndex + 1);
    }

    public void onBeforeButtonClick(View view) {
        if (view.getId() == shutterLeftButton.getId())
            setStateOfTextViews(shutterIndex - 1, isoIndex, apertureIndex);
        else if (view.getId() == isoLeftButton.getId())
            setStateOfTextViews(shutterIndex, isoIndex - 1, apertureIndex);
        else
            setStateOfTextViews(shutterIndex, isoIndex, apertureIndex - 1);
    }

    public static Sensor getmLight() {
        return mLight;
    }
}