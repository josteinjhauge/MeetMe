package com.example.meetme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyNotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startTime){
        Log.i("MyNotificationService", "I NotifikasjonService");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, MeetingsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, i, 0);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Møte notifikasjon!")
                .setContentText("Du har et møte idag...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
        Log.i("notify","Notification sent.");
        return super.onStartCommand(intent, flags, startTime);
    }
}
