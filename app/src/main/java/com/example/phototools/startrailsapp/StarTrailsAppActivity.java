package com.example.phototools.startrailsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.phototools.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class StarTrailsAppActivity extends AppCompatActivity {


    private RelativeLayout relativeLayout;
    private SeekBar seekBar;
    private TextView textView;
    private TextView requiredTimeTextView;

    public static float angle = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_trails_app);

        Objects.requireNonNull(getSupportActionBar()).show();
        getSupportActionBar().setTitle(R.string.starTrailsAppTextView);

        relativeLayout  = findViewById(R.id.idRLView);

        DrawingTable paintView = new DrawingTable(this);
        paintView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        relativeLayout.addView(paintView);

        seekBar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.textView);
        requiredTimeTextView = findViewById(R.id.requiredTimeTextView);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        textView.setText(String.format(Locale.US, "%.2f°", (float)i / 1000.0f));
                        angle = (float) i / 1000.0f;

                        Date date = new Date((long)((float) i / 1000.0f) * 240000L);

                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                        requiredTimeTextView.setText(sdf.format(date));

                        Bitmap bm = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(), Bitmap.Config.ARGB_4444);
                        paintView.draw(new Canvas(bm));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        angle = 0.0f;
    }
}
