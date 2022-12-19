package com.example.phototools.timerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.phototools.R;
import com.example.phototools.timerapp.DatePickerFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import org.shredzone.commons.suncalc.MoonTimes;
import org.shredzone.commons.suncalc.SunTimes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class SetTimerActivity extends AppCompatActivity {

    Calendar date;
    String description = "Nincs megjegyzés";
    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.", Locale.ENGLISH);
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

    public static double[] COORD = new double[]{0, 0};
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    Spinner spinner;
    String[] events = { "Csillagászati hajnal", "Csillagászati alkonyat", "Tengerészeti hajnal",
            "Tengerészeti alkonyat", "Kék hajnal", "Kék alkonyat", "Arany hajnal", "Arany alkonyat",
            "Napkelte", "Naplemente", "Holdkelte", "Holdnyugta" };

    RadioButton radioButton0;
    RadioButton radioButton1;

    LinearLayout normalWindow0;
    LinearLayout normalWindow1;
    LinearLayout astronomicalWindow;

    TextView setTimerDateTextView;
    TextView setTimerTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);

        Objects.requireNonNull(getSupportActionBar()).hide();


        spinner = findViewById(R.id.spinner);
        radioButton0 = findViewById(R.id.timerRadioButton0);
        radioButton1 = findViewById(R.id.timerRadioButton1);
        normalWindow0 = findViewById(R.id.normalWindow0);
        normalWindow1 = findViewById(R.id.normalWindow1);
        astronomicalWindow = findViewById(R.id.astronomicalWindow);
        setTimerDateTextView = findViewById(R.id.setTimerDateTextView);
        setTimerTimeTextView = findViewById(R.id.setTimerTimeTextView);

        date = Calendar.getInstance();
        setTimerDateTextView.setText(dateFormat.format(date.getTime()));
        setTimerTimeTextView.setText(timeFormat.format(date.getTime()));

        radioButton0.setOnClickListener(view -> {
            normalWindow0.setVisibility(View.VISIBLE);
            normalWindow1.setVisibility(View.VISIBLE);
            astronomicalWindow.setVisibility(View.GONE);
        });
        radioButton1.setOnClickListener(view -> {
            normalWindow0.setVisibility(View.GONE);
            normalWindow1.setVisibility(View.GONE);
            astronomicalWindow.setVisibility(View.VISIBLE);
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(SetTimerActivity.this, R.layout.simple_spinner_item, events);
        adapter.setDropDownViewResource(R.layout.my_support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                description = adapterView.getItemAtPosition(i).toString();

                getAstronomicalEventDate(description);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                        @NonNull
                        @Override
                        public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                            return null;
                        }

                        @Override
                        public boolean isCancellationRequested() {
                            return false;
                        }
                    })
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            COORD[0] = location.getLatitude();
                            COORD[1] = location.getLongitude();
                        }
                        // else tudatni kellene a felhasználóval hogy kötelező a permission
                    });
        } else {
            askPermission();
        }
    }


    private void askPermission() {
        ActivityCompat.requestPermissions(SetTimerActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }


    private void getAstronomicalEventDate(String value) {

        Calendar c = date;

        SunTimes.Parameters base = SunTimes.compute()
                .on(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))
                .at(COORD);
        MoonTimes.Parameters moonBase = MoonTimes.compute()
                .on(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))
                .at(COORD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (value.equals(events[0])) {
                date.setTimeInMillis(base.copy().twilight(SunTimes.Twilight.ASTRONOMICAL).execute().getRise().toInstant().toEpochMilli());
//                return Date.from(base.copy().twilight(SunTimes.Twilight.ASTRONOMICAL).execute().getRise()
//                        .toInstant());
            } else if (value.equals(events[1])) {
                date.setTimeInMillis(base.copy().twilight(SunTimes.Twilight.ASTRONOMICAL).execute().getSet().toInstant().toEpochMilli());
//                return Date.from(base.copy().twilight(SunTimes.Twilight.ASTRONOMICAL).execute().getSet()
//                        .toInstant());
            } else if (value.equals(events[2])) {
                date.setTimeInMillis(base.copy().twilight(SunTimes.Twilight.NAUTICAL).execute().getRise().toInstant().toEpochMilli());
//                return Date.from(base.copy().twilight(SunTimes.Twilight.NAUTICAL).execute().getRise()
//                        .toInstant());
            } else if (value.equals(events[3])) {
                date.setTimeInMillis(base.copy().twilight(SunTimes.Twilight.NAUTICAL).execute().getSet().toInstant().toEpochMilli());
//                return Date.from(base.copy().twilight(SunTimes.Twilight.NAUTICAL).execute().getSet()
//                        .toInstant());
            } else if (value.equals(events[4])) {
                date.setTimeInMillis(base.copy().twilight(SunTimes.Twilight.BLUE_HOUR).execute().getRise().toInstant().toEpochMilli());
//                return Date.from(base.copy().twilight(SunTimes.Twilight.BLUE_HOUR).execute().getRise()
//                        .toInstant());
            } else if (value.equals(events[5])) {
                date.setTimeInMillis(base.copy().twilight(SunTimes.Twilight.BLUE_HOUR).execute().getSet().toInstant().toEpochMilli());
//                return Date.from(base.copy().twilight(SunTimes.Twilight.BLUE_HOUR).execute().getSet()
//                        .toInstant());
            } else if (value.equals(events[6])) {
                date.setTimeInMillis(base.copy().twilight(SunTimes.Twilight.GOLDEN_HOUR).execute().getRise().toInstant().toEpochMilli());
//                return Date.from(base.copy().twilight(SunTimes.Twilight.GOLDEN_HOUR).execute().getRise()
//                        .toInstant());
            } else if (value.equals(events[7])) {
                date.setTimeInMillis(base.copy().twilight(SunTimes.Twilight.GOLDEN_HOUR).execute().getSet().toInstant().toEpochMilli());
//                return Date.from(base.copy().twilight(SunTimes.Twilight.GOLDEN_HOUR).execute().getSet()
//                        .toInstant());
            } else if (value.equals(events[8])) {
                date.setTimeInMillis(base.copy().execute().getRise().toInstant().toEpochMilli());
//                return Date.from(base.copy().execute().getRise()
//                        .toInstant());
            } else if (value.equals(events[9])) {
                date.setTimeInMillis(base.copy().execute().getSet().toInstant().toEpochMilli());
//                return Date.from(base.copy().execute().getSet()
//                        .toInstant());
            } else if (value.equals(events[10])) {
                date.setTimeInMillis(moonBase.copy().execute().getRise().toInstant().toEpochMilli());
//                return Date.from(moonBase.copy().execute().getRise()
//                        .toInstant());
            } else if (value.equals(events[11])) {
                date.setTimeInMillis(moonBase.copy().execute().getSet().toInstant().toEpochMilli());
//                return Date.from(moonBase.copy().execute().getSet()
//                        .toInstant());
            }
        }

    }

    public void onSetTimerOKButtonClick(View view) {
        Intent replyIntent = new Intent();
        replyIntent.putExtra("DATE", date.getTime().getTime());
        replyIntent.putExtra("DESCRIPTION", description);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    public void onSetTimerDateButtonClick(View view) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "DatePicker");
    }

    public void onSetTimerTimeButtonClick(View view) {

        DialogFragment fragment = new TimePickerFragment();
        fragment.show(getSupportFragmentManager(), "TimePicker");
    }

    public void processDatePickerResult(int y, int m, int d) {
        date.set(Calendar.YEAR, y);
        date.set(Calendar.MONTH, m);
        date.set(Calendar.DAY_OF_MONTH, d);

        setTimerDateTextView.setText(dateFormat.format(date.getTime()));
    }

    public void processTimePickerResult(int i, int i1) {
        date.set(Calendar.HOUR_OF_DAY, i);
        date.set(Calendar.MINUTE, i1);
        date.set(Calendar.SECOND, 0);

        setTimerTimeTextView.setText(timeFormat.format(date.getTime()));
    }
}