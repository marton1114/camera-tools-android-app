package com.example.phototools.lightpollutionapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phototools.R;
import com.example.phototools.timelapseapp.TimeLapseAppActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;


import org.shredzone.commons.suncalc.MoonIllumination;
import org.shredzone.commons.suncalc.MoonPosition;
import org.shredzone.commons.suncalc.SunPosition;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class LightPollutionAppActivity extends AppCompatActivity {

    BarChart barChart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelsNames;
    ArrayList<HourLightData> hourLightDataArrayList = new ArrayList<>();


    TextView locationTextView, dateTextView;
    Switch negativeValueSwitch;

    boolean withNegativeValues = false;

    private Calendar date = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);

    public static double[] COORD = new double[]{0, 0};
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_pollution_app);

        Objects.requireNonNull(getSupportActionBar()).show();
        getSupportActionBar().setTitle(R.string.lightPollutionAppTextView);


        barChart = findViewById(R.id.barChart);
        locationTextView = findViewById(R.id.locationTextView);
        dateTextView = findViewById(R.id.dateTextView);
        negativeValueSwitch = findViewById(R.id.negativeValueSwitch);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        statusCheck();
        getCurrentLocation();

        dateTextView.setText(dateFormat.format(date.getTime()));

        processDatePickerResult(date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH), true);

        negativeValueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                withNegativeValues = b;
                
                processDatePickerResult(date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH), false);
            }
        });
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.noGPSMessage)
                .setCancelable(false)
                .setPositiveButton(R.string.noGPSMessageYes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.noGPSMessageNo, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
                            Geocoder geocoder = new Geocoder(LightPollutionAppActivity.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(),
                                        location.getLongitude(),
                                        1);
                                COORD[0] = addresses.get(0).getLatitude();
                                COORD[1] = addresses.get(0).getLongitude();
                                locationTextView.setText(String.format(Locale.US,
                                        "%.2f, %.2f\n %s",
                                        COORD[0], COORD[1],
                                        addresses.get(0).getAddressLine(0)));
                            } catch (IOException e) {
                                COORD[0] = location.getLatitude();
                                COORD[1] = location.getLongitude();

                                locationTextView.setText(String.format(Locale.US, "%.2f, %.2f",
                                        COORD[0], COORD[1]));
                            }

                            processDatePickerResult(date.get(Calendar.YEAR),
                                    date.get(Calendar.MONTH),
                                    date.get(Calendar.DAY_OF_MONTH), false);
                        }
                        // else tudatni kellene a felhasználóval hogy kötelező a permission
                    });
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(LightPollutionAppActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        } else {
            Toast.makeText(this, "Engedély szükséges!", Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onDateButtonClick(View view) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "DatePicker");

    }

    public void onLocationButtonClick(View view) {
        final Dialog dialog = new Dialog(LightPollutionAppActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.my_coord_picker_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button myCoordPickerDialogButton = dialog.findViewById(R.id.myCoordPickerDialogButton);
        Button myCoordPickerDialogPlusButton = dialog.findViewById(R.id.myCoordPickerDialogPlusButton);
        final TextView myCoordPickerDialogTextView0 = dialog.findViewById(R.id.myCoordPickerDialogTextView0);
        final TextView myCoordPickerDialogTextView1 = dialog.findViewById(R.id.myCoordPickerDialogTextView1);

        myCoordPickerDialogButton.setOnClickListener(view1 -> {
            if (Objects.requireNonNull(myCoordPickerDialogTextView0.getText()).toString().trim().isEmpty()) {
                myCoordPickerDialogTextView0.setText("0");
            }
            if (Objects.requireNonNull(myCoordPickerDialogTextView1.getText()).toString().trim().isEmpty()) {
                myCoordPickerDialogTextView1.setText("0");
            }

            COORD[0] = Float.parseFloat(myCoordPickerDialogTextView0.getText().toString());
            COORD[1] = Float.parseFloat(myCoordPickerDialogTextView1.getText().toString());

            if (COORD[0] > 90.0f) {
                COORD[0] = 90.0f;
            } else if (COORD[0] < -90.0f) {
                COORD[0] = -90.0f;
            }

            if (COORD[1] > 180.0f) {
                COORD[1] = 180.0f;
            } else if (COORD[1] < -180.0f) {
                COORD[1] = -180;
            }


            processDatePickerResult(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), false);

            Geocoder geocoder = new Geocoder(LightPollutionAppActivity.this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(COORD[0], COORD[1],
                        1);
                locationTextView.setText(String.format(Locale.US,
                        "%f, %f\n %s",
                        COORD[0], COORD[1],
                        addresses.get(0).getAddressLine(0)));
            } catch (Exception e) {
                locationTextView.setText(String.format(Locale.US, "%.2f, %.2f",
                        COORD[0], COORD[1]));
            }
            dialog.dismiss();
        });

        myCoordPickerDialogPlusButton.setOnClickListener(view1 -> {
            getCurrentLocation();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void onLastDayButtonClick(View view) {
        date.add(Calendar.DAY_OF_MONTH, -1);


        processDatePickerResult(date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH), false);
    }

    public void onNextDayButtonClick(View view) {
        date.add(Calendar.DAY_OF_MONTH, 1);

        System.out.println("NEXT DAY BUTTON");
        processDatePickerResult(date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH), false);
    }

    private void fillHourLights(double[] coords, int year, int month, int day) {
        hourLightDataArrayList.clear();

        MoonIllumination.Parameters parameters = MoonIllumination.compute()
                .on(year, month + 1, day);
        MoonPosition.Parameters parameters1 = MoonPosition.compute()
                .at(coords);

        SunPosition.Parameters sunParameters = SunPosition.compute()
                .at(coords);

        double moonPercent = parameters.execute().getFraction();

        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        do {
            int hour = date.get(Calendar.HOUR_OF_DAY);
            int minute = date.get(Calendar.MINUTE);
            double sunDegree = sunParameters.on(year, month + 1, day, hour, 0, 0).execute().getAltitude();

            double moonDegree = parameters1.on(year, month + 1, day, hour, 0, 0).execute().getAltitude();

            int moonLight = (int)(moonPercent * moonDegree);
            int sunLight = (int)sunDegree;

            hourLightDataArrayList.add(new HourLightData(
                    String.format(Locale.US, "%02d:%02d", hour, minute),
                    sunLight, moonLight));
            date.add(Calendar.HOUR_OF_DAY, 1);
        } while (date.get(Calendar.HOUR_OF_DAY) != 0);
        date.add(Calendar.HOUR_OF_DAY, -1);

    }

    public void processDatePickerResult(int y, int m, int d, boolean firstRun) {
        date.set(y, m, d);
        dateTextView.setText(dateFormat.format(date.getTime()));

        fillHourLights(COORD, y, m + 1, d);

        barEntryArrayList = new ArrayList<>();
        labelsNames = new ArrayList<>();


        for (int i = 0; i < hourLightDataArrayList.size(); i++) {
            String hour = hourLightDataArrayList.get(i).getHour();

            int sunLight;
            int moonLight;

            if (firstRun) {
                sunLight = 0;
                moonLight = 0;
            } else {
                sunLight = hourLightDataArrayList.get(i).getSunLight();
                moonLight = hourLightDataArrayList.get(i).getMoonLight();
            }

            barEntryArrayList.add(new BarEntry(i,
                    new float[]{moonLight, sunLight}
            ));
            labelsNames.add(hour);
        }

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "");
        barDataSet.setColors(
                ColorTemplate.colorWithAlpha(ColorTemplate.rgb("78A0FF"),
                Integer.MAX_VALUE),
                ColorTemplate.colorWithAlpha(ColorTemplate.rgb("FCBA03"),
                Integer.MAX_VALUE)
        );
        barDataSet.setStackLabels(new String[]{"Hold", "Nap"});
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(false);
        barChart.setData(barData);

        Legend l = barChart.getLegend();
        l.setTextSize(20f);
        l.setTextColor(Color.BLACK);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(20f);
        l.setFormToTextSpace(10);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextSize(20f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsNames));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setGridLineWidth(20);
        xAxis.setGridColor(ColorTemplate.colorWithAlpha(ColorTemplate.rgb("FFFFFF"), 100));
        xAxis.setGranularity(1.0f);
        xAxis.setLabelCount(labelsNames.size());
        xAxis.setLabelRotationAngle(270);

        YAxis axisLeft = barChart.getAxisLeft();
        YAxis axisRight = barChart.getAxisRight();
        
        if (withNegativeValues) {
            axisLeft.setAxisMinimum(-100);

        } else {
            axisLeft.setAxisMinimum(0);
        }
        axisLeft.setAxisMaximum(100);
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
//        axisLeft.setDrawLabels(false);
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawLabels(false);
        axisRight.setDrawGridLines(false);

        barChart.animateY(1000);
        barChart.zoomToCenter(0.5f, 1.0f);
        barChart.zoomToCenter(2.0f, 1.0f);
        barChart.setHighlightPerTapEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.invalidate();
    }
}