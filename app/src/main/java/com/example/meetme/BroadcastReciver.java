package com.example.meetme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadcastReciver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent){
        Log.i("BroadcastReceiver", "I broadcastReceiver");
        String time = intent.getStringExtra("time");

        Intent i = new Intent(context, SetPeriodiskMySMSService.class);
        i.putExtra("timeFromBroadcast", time);
        context.startService(i);
    }
}
