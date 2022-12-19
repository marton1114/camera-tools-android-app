package com.example.phototools.timerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.phototools.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;

public class TimerAppActivity extends AppCompatActivity {

    private TimerDatabase timerDatabase;

    private int requestCode = 1;

    RecyclerView recyclerView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode){
            if (resultCode == RESULT_OK){
                long millis = data.getLongExtra("DATE", 0L);
                String description = data.getStringExtra("DESCRIPTION");

                long timeNow = Calendar.getInstance().getTime().getTime();

                if (millis < timeNow) {
                    Toast.makeText(getApplicationContext(), R.string.toastMessage, Toast.LENGTH_SHORT).show();
                    return;
                }


                TimerItem ti = new TimerItem();
                ti.setDate(millis);
                ti.setDescription(description);

                new Thread(() -> {
                    timerDatabase.timerDao().insertListItem(ti);
                }).start();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_app);

        Objects.requireNonNull(getSupportActionBar()).show();
        getSupportActionBar().setTitle(R.string.timerAppTextView);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        timerDatabase = Room.databaseBuilder(
                this,
                TimerDatabase.class,
                "timer_db")
                .fallbackToDestructiveMigration()
                .build();

        timerDatabase.timerDao().getAllItems()
                .observe(this, timerItems -> {
                    recyclerView.setAdapter(new ViewAdapter(timerItems, timerDatabase.timerDao()));
                });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new Thread(() -> {
                    timerDatabase.timerDao()
                            .deleteListItem(((ViewAdapter.MyViewHolder)viewHolder).getListItem());
                }).start();
            }
        }).attachToRecyclerView(recyclerView);


    }

    public void onTimerAppPlusButtonClick(View view) {
        Intent intent = new Intent(this, SetTimerActivity.class);
        startActivityForResult(intent, requestCode);
    }
}