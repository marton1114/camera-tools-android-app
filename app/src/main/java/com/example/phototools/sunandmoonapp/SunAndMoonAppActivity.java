package com.example.phototools.sunandmoonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phototools.R;
import com.example.phototools.timerapp.TimerAppActivity;
import com.example.phototools.timerapp.TimerDatabase;
import com.example.phototools.timerapp.TimerItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import org.shredzone.commons.suncalc.MoonPhase;
import org.shredzone.commons.suncalc.MoonTimes;
import org.shredzone.commons.suncalc.SunPosition;
import org.shredzone.commons.suncalc.SunTimes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SunAndMoonAppActivity extends AppCompatActivity {

    private Calendar date = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);
    SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.ENGLISH);

    public static double[] COORD = new double[]{0, 0};
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 100;

    TextView sunAndMoonAppLocationTextView;
    TextView sunAndMoonAppDateTextView;

    TextView astRise, astSet, nautRise, nautSet, blueRise, blueSet, goldenRise, goldenSet,
            civilRise, civilSet, moonRise, moonSet;

    Button sunAndMoonAppPlusButton0, sunAndMoonAppPlusButton1, sunAndMoonAppPlusButton2,
            sunAndMoonAppPlusButton3, sunAndMoonAppPlusButton4, sunAndMoonAppPlusButton5,
            sunAndMoonAppPlusButton6, sunAndMoonAppPlusButton7, sunAndMoonAppPlusButton8,
            sunAndMoonAppPlusButton9, sunAndMoonAppPlusButton10, sunAndMoonAppPlusButton11;

    List<Date> dates = new ArrayList<Date>() {{
        add(null); add(null); add(null); add(null); add(null); add(null); add(null); add(null);
        add(null); add(null); add(null); add(null);
    }};

    TimerDatabase timerDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sun_and_moon_app);

        Objects.requireNonNull(getSupportActionBar()).show();
        getSupportActionBar().setTitle(R.string.sunAndMoonAppTextView);

        //  finding views by id
        {
            sunAndMoonAppLocationTextView = findViewById(R.id.sunAndMoonAppLocationTextView);
            sunAndMoonAppDateTextView = findViewById(R.id.sunAndMoonAppDateTextView);
            astRise = findViewById(R.id.astRise);
            astSet = findViewById(R.id.astSet);
            nautRise = findViewById(R.id.nautRise);
            nautSet = findViewById(R.id.nautSet);
            blueRise = findViewById(R.id.blueRise);
            blueSet = findViewById(R.id.blueSet);
            goldenRise = findViewById(R.id.goldenRise);
            goldenSet = findViewById(R.id.goldenSet);
            civilRise = findViewById(R.id.civilRise);
            civilSet = findViewById(R.id.civilSet);

            moonRise = findViewById(R.id.moonRise);
            moonSet = findViewById(R.id.moonSet);

            sunAndMoonAppPlusButton0 = findViewById(R.id.sunAndMoonAppPlusButton0);
            sunAndMoonAppPlusButton1 = findViewById(R.id.sunAndMoonAppPlusButton1);
            sunAndMoonAppPlusButton2 = findViewById(R.id.sunAndMoonAppPlusButton2);
            sunAndMoonAppPlusButton3 = findViewById(R.id.sunAndMoonAppPlusButton3);
            sunAndMoonAppPlusButton4 = findViewById(R.id.sunAndMoonAppPlusButton4);
            sunAndMoonAppPlusButton5 = findViewById(R.id.sunAndMoonAppPlusButton5);
            sunAndMoonAppPlusButton6 = findViewById(R.id.sunAndMoonAppPlusButton6);
            sunAndMoonAppPlusButton7 = findViewById(R.id.sunAndMoonAppPlusButton7);
            sunAndMoonAppPlusButton8 = findViewById(R.id.sunAndMoonAppPlusButton8);
            sunAndMoonAppPlusButton9 = findViewById(R.id.sunAndMoonAppPlusButton9);
            sunAndMoonAppPlusButton10 = findViewById(R.id.sunAndMoonAppPlusButton10);
            sunAndMoonAppPlusButton11 = findViewById(R.id.sunAndMoonAppPlusButton11);

        }

        //  on click listeners
        {
            timerDatabase = Room.databaseBuilder(
                            this,
                            TimerDatabase.class,
                            "timer_db")
                    .fallbackToDestructiveMigration()
                    .build();

            Date timeNow = Calendar.getInstance().getTime();

            Intent intent = new Intent(this, TimerAppActivity.class);
            sunAndMoonAppPlusButton0.setOnClickListener(view -> {
                if (dates.get(0) != null) {
                    if (dates.get(0).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(0).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_astronomicalRise));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            sunAndMoonAppPlusButton1.setOnClickListener(view -> {
                if (dates.get(1) != null) {
                    if (dates.get(1).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(1).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_nauticalRise));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            sunAndMoonAppPlusButton2.setOnClickListener(view -> {
                if (dates.get(2) != null) {
                    if (dates.get(2).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(2).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_blueRise));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            sunAndMoonAppPlusButton3.setOnClickListener(view -> {
                if (dates.get(3) != null) {
                    if (dates.get(3).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(3).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_sunRiseLabel));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            sunAndMoonAppPlusButton4.setOnClickListener(view -> {
                if (dates.get(4) != null) {
                    if (dates.get(4).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(4).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_goldenRiseLabel));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            sunAndMoonAppPlusButton5.setOnClickListener(view -> {
                if (dates.get(5) != null) {
                    if (dates.get(5).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(5).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_sunSetLabel));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            sunAndMoonAppPlusButton6.setOnClickListener(view -> {
                if (dates.get(6) != null) {
                    if (dates.get(6).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(6).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_goldenSetLabel));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            sunAndMoonAppPlusButton7.setOnClickListener(view -> {
                if (dates.get(7) != null) {
                    if (dates.get(7).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(7).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_blueSetLabel));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            sunAndMoonAppPlusButton8.setOnClickListener(view -> {
                if (dates.get(8) != null) {
                    if (dates.get(8).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(8).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_nauticalSetLabel));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            sunAndMoonAppPlusButton9.setOnClickListener(view -> {
                if (dates.get(9) != null) {
                    if (dates.get(9).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(9).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_astronomicalSetLabel));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            sunAndMoonAppPlusButton10.setOnClickListener(view -> {
                if (dates.get(10) != null) {
                    if (dates.get(10).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(10).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_moonRiseLabel));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            sunAndMoonAppPlusButton11.setOnClickListener(view -> {
                if (dates.get(11) != null) {
                    if (dates.get(11).getTime() > timeNow.getTime()) {
                        TimerItem ti = new TimerItem();
                        ti.setDate(dates.get(11).getTime());
                        ti.setDescription(getString(R.string.sunAndMoonApp_moonSetLabel));

                        new Thread(() -> {
                            timerDatabase.timerDao().insertListItem(ti);
                        }).start();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getString(R.string.toastMessage),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        sunAndMoonAppDateTextView.setText(dateFormat.format(date.getTime()));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        statusCheck();
        getCurrentLocation();
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
                            Geocoder geocoder = new Geocoder(SunAndMoonAppActivity.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(),
                                        location.getLongitude(),
                                        1);
                                COORD[0] = addresses.get(0).getLatitude();
                                COORD[1] = addresses.get(0).getLongitude();
                                sunAndMoonAppLocationTextView.setText(String.format(Locale.US,
                                        "%.2f, %.2f\n %s",
                                        COORD[0], COORD[1],
                                        addresses.get(0).getAddressLine(0)));
                            } catch (IOException e) {
                                COORD[0] = location.getLatitude();
                                COORD[1] = location.getLongitude();

                                sunAndMoonAppLocationTextView.setText(String.format(Locale.US, "%.2f, %.2f",
                                        COORD[0], COORD[1]));
                            }

                            processDatePickerResult(date.get(Calendar.YEAR),
                                    date.get(Calendar.MONTH),
                                    date.get(Calendar.DAY_OF_MONTH), false);

                            updateResults();
                        }
                        // else tudatni kellene a felhasználóval hogy kötelező a permission
                    });
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(SunAndMoonAppActivity.this, new String[]
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

    public void onSunAndMoonAppDateButtonClick(View view) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "DatePicker");

    }

    public void onSunAndMoonAppLastDayButtonClick(View view) {
        date.add(Calendar.DAY_OF_MONTH, -1);


        processDatePickerResult(date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH), false);

        updateResults();
    }

    public void onSunAndMoonAppNextDayButtonClick(View view) {
        date.add(Calendar.DAY_OF_MONTH, 1);

        processDatePickerResult(date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH), false);

        updateResults();
    }

    public void processDatePickerResult(int i, int i1, int i2, boolean b) {
        date.set(i, i1, i2);
        sunAndMoonAppDateTextView.setText(dateFormat.format(date.getTime()));

        updateResults();
    }

    public void onSunAndMoonAppLocationButtonClick(View view) {
        final Dialog dialog = new Dialog(SunAndMoonAppActivity.this);

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

            Geocoder geocoder = new Geocoder(SunAndMoonAppActivity.this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(COORD[0], COORD[1],
                        1);
                sunAndMoonAppLocationTextView.setText(String.format(Locale.US,
                        "%f, %f\n %s",
                        COORD[0], COORD[1],
                        addresses.get(0).getAddressLine(0)));
                updateResults();
            } catch (Exception e) {
                sunAndMoonAppLocationTextView.setText(String.format(Locale.US, "%.2f, %.2f",
                        COORD[0], COORD[1]));
                updateResults();
            }
            dialog.dismiss();
        });
        myCoordPickerDialogPlusButton.setOnClickListener(view1 -> {
            getCurrentLocation();
            dialog.dismiss();
        });

        dialog.show();

    }

    public void updateResults() {
        SunTimes.Parameters base = SunTimes.compute()
                .on(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH))
                .at(COORD);
        MoonTimes.Parameters moonBase = MoonTimes.compute()
                .on(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH))
                .at(COORD);

        SunTimes astronomical = base
                .copy()
                .twilight(SunTimes.Twilight.ASTRONOMICAL)
                .execute();

        SunTimes nautical = base
                .copy()
                .twilight(SunTimes.Twilight.NAUTICAL)
                .execute();

        SunTimes blue = base
                .copy()
                .twilight(SunTimes.Twilight.BLUE_HOUR)
                .execute();

        SunTimes golden = base
                .copy()
                .twilight(SunTimes.Twilight.GOLDEN_HOUR)
                .execute();

        SunTimes civil = base
                .copy()
                .execute();

        MoonTimes moonTimes = moonBase
                .copy()
                .execute();

        dates.set(0, astronomical.getRise());
        dates.set(1, nautical.getRise());
        dates.set(2,blue.getRise());
        dates.set(3, civil.getRise());
        dates.set(4, golden.getRise());
        dates.set(5, civil.getSet());
        dates.set(6, golden.getSet());
        dates.set(7, blue.getSet());
        dates.set(8, nautical.getSet());
        dates.set(9, astronomical.getSet());
        dates.set(10, moonTimes.getRise());
        dates.set(11, moonTimes.getSet());
        astRise.setText((dates.get(0) != null) ? sdf.format(dates.get(0)) : "");
        astSet.setText((dates.get(1) != null) ? sdf.format(dates.get(1)) : "");
        nautRise.setText((dates.get(2) != null) ? sdf.format(dates.get(2)) : "");
        nautSet.setText((dates.get(3) != null) ? sdf.format(dates.get(3)) : "");
        blueRise.setText((dates.get(4) != null) ? sdf.format(dates.get(4)) : "");
        blueSet.setText((dates.get(5) != null) ? sdf.format(dates.get(5)) : "");
        goldenRise.setText((dates.get(6) != null) ? sdf.format(dates.get(6)) : "");
        goldenSet.setText((dates.get(7) != null) ? sdf.format(dates.get(7)) : "");
        civilRise.setText((dates.get(8) != null) ? sdf.format(dates.get(8)) : "");
        civilSet.setText((dates.get(9) != null) ? sdf.format(dates.get(9)) : "");
        moonRise.setText((dates.get(10) != null) ? sdf.format(dates.get(10)) : "");
        moonSet.setText((dates.get(11) != null) ? sdf.format(dates.get(11)) : "");
        sunAndMoonAppPlusButton0.setVisibility((dates.get(0) != null) ? View.VISIBLE : View.INVISIBLE);
        sunAndMoonAppPlusButton1.setVisibility((dates.get(1) != null) ? View.VISIBLE : View.INVISIBLE);
        sunAndMoonAppPlusButton2.setVisibility((dates.get(2) != null) ? View.VISIBLE : View.INVISIBLE);
        sunAndMoonAppPlusButton3.setVisibility((dates.get(3) != null) ? View.VISIBLE : View.INVISIBLE);
        sunAndMoonAppPlusButton4.setVisibility((dates.get(4) != null) ? View.VISIBLE : View.INVISIBLE);
        sunAndMoonAppPlusButton5.setVisibility((dates.get(5) != null) ? View.VISIBLE : View.INVISIBLE);
        sunAndMoonAppPlusButton6.setVisibility((dates.get(6) != null) ? View.VISIBLE : View.INVISIBLE);
        sunAndMoonAppPlusButton7.setVisibility((dates.get(7) != null) ? View.VISIBLE : View.INVISIBLE);
        sunAndMoonAppPlusButton8.setVisibility((dates.get(8) != null) ? View.VISIBLE : View.INVISIBLE);
        sunAndMoonAppPlusButton9.setVisibility((dates.get(9) != null) ? View.VISIBLE : View.INVISIBLE);
        sunAndMoonAppPlusButton10.setVisibility((dates.get(10) != null) ? View.VISIBLE : View.INVISIBLE);
        sunAndMoonAppPlusButton11.setVisibility((dates.get(11) != null) ? View.VISIBLE : View.INVISIBLE);
    }
}