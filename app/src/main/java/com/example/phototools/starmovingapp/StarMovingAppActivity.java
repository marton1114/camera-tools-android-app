package com.example.phototools.starmovingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.phototools.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class StarMovingAppActivity extends AppCompatActivity {

    List<Float> cropFactors = new ArrayList<Float>() {{
       add(6.0f);
       add(5.6f);
       add(4.8f);
       add(4.5f);
       add(3.9f);
       add(2.7f);
       add(1.8f);
       add(1.7f);
       add(1.6f);
       add(1.5f);
       add(1.3f);
       add(1.0f);
    }};
    int cropIndex = 0;

    List<Integer> focalLengths = new ArrayList<Integer>() {{
        add(4);
        add(14);
        add(15);
        add(35);
        add(85);
        add(135);
        add(300);
    }};
    int focalIndex = 0;

    TextView cropTextView;
    TextView focalTextView;
    TextView maxTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_moving_app);

        Objects.requireNonNull(getSupportActionBar()).show();
        getSupportActionBar().setTitle(R.string.starMovingAppTextView);

        cropTextView = findViewById(R.id.cropTextView);
        focalTextView = findViewById(R.id.focalTextView);
        maxTimeTextView = findViewById(R.id.maxTimeTextView);

        updateTextViews();
    }

    private float getMaxTime(int focalLength, float cropFactor) {
        return 500.0f / (focalLength * cropFactor);
    }

    private void updateTextViews() {
        focalTextView.setText(String.format(Locale.US, "%dmm", (int) focalLengths.get(focalIndex)));
        cropTextView.setText(String.format(Locale.US, "%.1f", (float) cropFactors.get(cropIndex)));

        maxTimeTextView.setText(String.format(Locale.US,
                "%.2f", getMaxTime(focalLengths.get(focalIndex), cropFactors.get(cropIndex))));
    }

    public void onLastFocalClick(View view) {
        if (focalIndex > 0)
            focalIndex--;
        updateTextViews();
    }

    public void onNextFocalClick(View view) {
        if (focalIndex < focalLengths.size() - 1)
            focalIndex++;
        updateTextViews();
    }

    public void onLastCropClick(View view) {
        if (cropIndex > 0)
            cropIndex--;
        updateTextViews();
    }

    public void onNextCropClick(View view) {
        if (cropIndex < cropFactors.size() - 1)
            cropIndex++;
        updateTextViews();
    }
}