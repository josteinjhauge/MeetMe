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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SetPeriodiskService extends Service {
    ArrayList<Date> dates = new ArrayList<>();
    Date meetingDate;
    int date;
    int nowDate;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences sharedPrefs = getSharedPreferences("dates", MODE_PRIVATE);
        Gson gson = new Gson();
        // ContactNumbers
        String responseDates = sharedPrefs.getString("dateList", null);
        Type type = new TypeToken<ArrayList<Date>>() {}.getType();
        dates = gson.fromJson(responseDates, type);

        Calendar now = Calendar.getInstance();
        Log.d("Calendar", "Now calendar: " + now);
        Date utilDate = now.getTime();
        Log.i("Date", "Dagens dato fra calendar: " + utilDate);

        if (dates != null) {
            for (int f = 0; f < dates.size(); f++) {
                Log.i("notifikasjon", "henter datoer... \n" +
                        dates.get(f).toString());
                meetingDate = dates.get(f);

                SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
                date = Integer.parseInt(sdf.format(meetingDate));
                Log.i("Format", "meetingDate formated: " + date);

                nowDate = Integer.parseInt(sdf.format(utilDate));
                Log.i("DateFormat", "NowDate formated: " + nowDate);

                if (nowDate == date) {
                    Log.i("DagSjekk", "nowDate og date er den samme: ");
                    Calendar cal = Calendar.getInstance();
                    // TODO: til Torunn bytt klokeslett under for å teste notifikasjon!
                    cal.set(Calendar.HOUR_OF_DAY, 6);
                    cal.set(Calendar.MINUTE, 00);
                    cal.set(Calendar.SECOND, 0);

                    // TODO: Litt usikker på om denne funker som den skal
                    if (now.after(cal)) {
                        Log.d("SE", "Added a day ");
                        cal.add(Calendar.DATE, 1); // 1 er dag man legger til
                    }

                    Intent i = new Intent(this, MyNotificationService.class);
                    PendingIntent pendingIntent = PendingIntent.getService(this,
                            0, i, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                            cal.getTimeInMillis(), pendingIntent);
                    Log.i("Alarm", "Alarm er satt til 06:00 hver dag");
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
