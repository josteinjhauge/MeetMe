package com.example.meetme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SetPeriodiskMySMSService extends Service {
    String time;
    Date timeAsTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        time = sharedPrefs.getString("alarmTime", "06:00");
        Log.d("Time", "time inne i periodiskSMSService: " + time);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");

            format.setTimeZone(TimeZone.getDefault());
            timeAsTime = new SimpleDateFormat("HH:mm").parse(time);
            Log.d("Time", "time i date format: " + format.format(timeAsTime));
        } catch (ParseException e) {
            Log.d("ParseException", "onStartCommand: coudnt parse time from string to dateformat HH:mm");
        }

        Date today = Calendar.getInstance().getTime();
        System.out.println("idag: " + today);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.setTime(timeAsTime); // TODO: vet denne kræsjer programmet
        // bytter man ut 6 med Integer.parseInt(time) så kræsjer appen

        Intent i = new Intent(this, MySMSService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,
                0, i, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.i("SMS", "SMS er satt til å sende: " + timeAsTime );


        return super.onStartCommand(intent, flags, startId);
    }

}
