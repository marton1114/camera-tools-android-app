package com.example.phototools.lightsapp;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.phototools.MainActivity;
import com.example.phototools.R;

import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class LightsAppActivity extends AppCompatActivity {

    Button torchButton;

    private static final String SHARED_PREF_FILE = "";
    private static final String TORCH_SHRPREF_KEY = "";

    private int lastValue;
    private boolean buttonPushed = false;
    int mDefaultColor;

    ConstraintLayout background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights_app);

        Objects.requireNonNull(getSupportActionBar()).show();
        getSupportActionBar().setTitle(R.string.lightsAppTextView);

        background = findViewById(R.id.background);
    }

    public void onColorPickerButtonClick(View view) {
        openColorPicker();
    }

    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                background.setBackgroundColor(mDefaultColor);
            }
        });
        colorPicker.show();
    }

    public void onBrightnessButtonClick(View view) {
        if (hasWritePermission(this)) {
            if (! buttonPushed) {
                changeBrightness(this, 255);
            } else {
                changeBrightness(this, lastValue);
            }
        } else {
            changeWritePermission(this);
        }
    }

    public void onTorchButtonClick(View view) {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_FILE, 0);
        boolean torch = prefs.getBoolean(TORCH_SHRPREF_KEY, true);
        CameraManager camManager =
                (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        try {
            cameraId = camManager.getCameraIdList()[0];
            camManager.setTorchMode(cameraId, torch);
            torch = !torch;

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // torchButton.setText("Torch " + (torch ? "on" : "off"));
        // Save count back to prefs.
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(TORCH_SHRPREF_KEY, torch);
        prefEditor.apply();
    }

    private void changeWritePermission(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        startActivity(intent);
    }

    private void changeBrightness(Activity activity, int value) {
        Settings.System.putInt(
                this.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        );

        if (! buttonPushed) {
            try {
                lastValue = Settings.System.getInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }

        Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);

        buttonPushed = ! buttonPushed;
    }

    private boolean hasWritePermission(Activity activity) {
        return Settings.System.canWrite(activity);
    }
}