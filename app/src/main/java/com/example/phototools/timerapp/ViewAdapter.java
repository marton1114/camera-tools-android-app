package com.example.phototools.timerapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phototools.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.MyViewHolder> {

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer_app_item_layout,
                parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Date date = new Date(data.get(position).getDate());
        holder.dat.setText(dateFormat.format(date));
        holder.tim.setText(timeFormat.format(date));
        holder.desc.setText(data.get(position).getDescription());
        holder.alarmSwitch.setChecked(data.get(position).isSwitchValue());
    }

    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.", Locale.ENGLISH);
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    SimpleDateFormat format = new SimpleDateFormat("yyy.MM.dd.HH:mm", Locale.ENGLISH);
    private List<TimerItem> data;
    private TimerDao dao;

    public ViewAdapter(List<TimerItem> data, TimerDao dao) {
        this.data = data;
        this.dao = dao;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dat, tim, desc;
        Switch alarmSwitch;
        AlarmManager alarmManager;
        PendingIntent pendingIntent;

        public MyViewHolder(View view) {
            super(view);
            MyViewHolder.this.dat = view.findViewById(R.id.cardViewDateTextView);
            MyViewHolder.this.tim = view.findViewById(R.id.cardViewTimeTextView);
            MyViewHolder.this.desc = view.findViewById(R.id.cardViewDescriptionTextView);
            MyViewHolder.this.alarmSwitch = view.findViewById(R.id.alarmSwitch);

            MyViewHolder.this.alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);

            createNotificationChannel(view);


            MyViewHolder.this.alarmSwitch.setOnClickListener(view1 -> {

                data.get(getLayoutPosition()).setSwitchValue(
                        ! data.get(getLayoutPosition()).isSwitchValue()
                );

                if (data.get(getLayoutPosition()).isSwitchValue()) {
                    setAlarm(view);
                } else {
                    cancelAlarm(view);
                }
                new Thread(() -> dao.updateListItem(data.get(getLayoutPosition()))).start();
            });
        }

        private void cancelAlarm(View view) {
            Intent intent = new Intent(view.getContext(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, intent, 0);
            if (alarmManager == null) {
                alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
            }

            alarmManager.cancel(pendingIntent);
        }

        private void setAlarm(View view) {
            alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(view.getContext(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, intent, 0);

            Calendar calendar = Calendar.getInstance();

            String date = MyViewHolder.this.dat.getText().toString();
            String time = MyViewHolder.this.tim.getText().toString();
            try {
                calendar.setTime(format.parse(date + time));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        private void createNotificationChannel(View view) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "notifchannelReminderChannel";
                String description = "Channel for Alarm Manager";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("notifchannel", name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = view.getContext().getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

            }
        }

        public TimerItem getListItem() {
            return data.get(getLayoutPosition());
        }
    }

}
