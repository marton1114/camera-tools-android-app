package com.example.phototools.timelapseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phototools.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class TimeLapseAppActivity extends AppCompatActivity {

    TextView videoLengthTextView;
    TextView fpsTextView;
    TextView mbTextView;

    TextView timeLapseResult0;
    TextView timeLapseResult1;

    SimpleDateFormat sdf = new SimpleDateFormat("H'h 'm'm 's's '", Locale.ENGLISH);

    int fps = 10;
    float mb = 5.0f;
    int seconds = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_lapse_app);

        Objects.requireNonNull(getSupportActionBar()).show();
        getSupportActionBar().setTitle(R.string.timeLapseAppTextView);

        videoLengthTextView = findViewById(R.id.videoLengthTextView);
        fpsTextView = findViewById(R.id.fpsTextView);
        mbTextView = findViewById(R.id.mbTextView);
        timeLapseResult0 = findViewById(R.id.timeLapseResult0);
        timeLapseResult1 = findViewById(R.id.timeLapseResult1);

        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public void onVideoLengthTextViewClick(View view) {
        showMyTimePickerDialog();
    }

    public void onFOSTextViewClick(View view) {
        showMyFPSPickerDialog();
    }

    public void onMBTextViewClick(View view) {
        showMyMBPickerDialog();
    }

    private void showMyTimePickerDialog() {
        final Dialog dialog = new Dialog(TimeLapseAppActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.my_time_picker_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText myTimePickerDialogH = dialog.findViewById(R.id.myTimePickerDialogH);
        final EditText myTimePickerDialogM = dialog.findViewById(R.id.myTimePickerDialogM);
        final EditText myTimePickerDialogS = dialog.findViewById(R.id.myTimePickerDialogS);
        Button myTimePickerDialogButton = dialog.findViewById(R.id.myTimePickerDialogOK);


        myTimePickerDialogButton.setOnClickListener(view -> {
            if (Objects.requireNonNull(myTimePickerDialogH.getText()).toString().trim().isEmpty()) {
                myTimePickerDialogH.setText("0");
            }
            if (Objects.requireNonNull(myTimePickerDialogM.getText()).toString().trim().isEmpty()) {
                myTimePickerDialogM.setText("0");
            }
            if (Objects.requireNonNull(myTimePickerDialogS.getText()).toString().trim().isEmpty()) {
                myTimePickerDialogS.setText("0");
            }

            int hour = Integer.parseInt(myTimePickerDialogH.getText().toString());
            int minute = Integer.parseInt(myTimePickerDialogM.getText().toString());
            int second = Integer.parseInt(myTimePickerDialogS.getText().toString());

            seconds = hour * 60 * 60 + minute * 60 + second;
            calcResult();
            Date date = new Date(hour * 60 * 60 * 1000L + minute * 60 * 1000L + second * 1000L);

            videoLengthTextView.setText(sdf.format(date));

            dialog.dismiss();
        });

        dialog.show();
    }

    private void showMyFPSPickerDialog() {
        final Dialog dialog = new Dialog(TimeLapseAppActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.my_fps_picker_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button myFPSPickerDialogButton = dialog.findViewById(R.id.myMBPickerDialogButton);
        final TextView myFPSPickerDialogTextView = dialog.findViewById(R.id.myFPSPickerDialogTextView);

        myFPSPickerDialogButton.setOnClickListener(view -> {
            if (Objects.requireNonNull(myFPSPickerDialogTextView.getText()).toString().trim().isEmpty()) {
                myFPSPickerDialogTextView.setText("0");
            }

            fps = Integer.parseInt(myFPSPickerDialogTextView.getText().toString());
            calcResult();

            fpsTextView.setText(String.format("%s FPS", myFPSPickerDialogTextView.getText().toString()));
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showMyMBPickerDialog() {
        final Dialog dialog = new Dialog(TimeLapseAppActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.my_mb_picker_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button myMBPickerDialogButton = dialog.findViewById(R.id.myMBPickerDialogButton);
        final TextView myMBPickerDialogTextView = dialog.findViewById(R.id.myMBPickerDialogTextView);

        myMBPickerDialogButton.setOnClickListener(view -> {
            if (Objects.requireNonNull(myMBPickerDialogTextView.getText()).toString().trim().isEmpty()) {
                myMBPickerDialogTextView.setText("0");
            }

            mb = Float.parseFloat(myMBPickerDialogTextView.getText().toString());
            calcResult();

            mbTextView.setText(String.format("%s MB", myMBPickerDialogTextView.getText().toString()));
            dialog.dismiss();
        });

        dialog.show();
    }

    private void calcResult() {
        int result0 = seconds * fps;
        float result1 = seconds * fps * mb;
        timeLapseResult0.setText(Integer.toString(result0));
        timeLapseResult1.setText(String.format("%s MB", Float.toString(result1)));
    }
}