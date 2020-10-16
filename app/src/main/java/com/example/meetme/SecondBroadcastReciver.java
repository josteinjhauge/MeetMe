package com.example.meetme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class SecondBroadcastReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast", "I BroadcastReciverNotification");

        Intent i = new Intent(context, SetPeriodiskService.class);
        context.startService(i);
    }
}
