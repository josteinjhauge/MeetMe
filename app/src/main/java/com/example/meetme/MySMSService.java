package com.example.meetme;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MySMSService extends Service {
    String text;
    String number;
    String time;
    ArrayList<String> contactNumbers = new ArrayList<>();
    ArrayList<String> meetingTimes = new ArrayList<>();


    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startTid) {
        SharedPreferences sharedSMSPrefs = getSharedPreferences("SMSInfo", MODE_PRIVATE);
        Gson gson = new Gson();
        // ContactNumbers
        String responseContactNumbers = sharedSMSPrefs.getString("contactNumberList", null);
        Type numberType = new TypeToken<ArrayList<String>>() {}.getType();
        contactNumbers = gson.fromJson(responseContactNumbers, numberType);

        // meetingTimes
        String responsTimes = sharedSMSPrefs.getString("meetingTimeList", null);
        Type timeType = new TypeToken<ArrayList<String>>() {}.getType();
        meetingTimes = gson.fromJson(responsTimes, timeType);

        Log.i("SMSService", "I SMS Service");

        try {
            sendSms();
        } catch (Exception e) {
            Log.d("TAG", "onStartCommand: sendSMS fail");
        }
        return super.onStartCommand(intent, flags, startTid);
    }

    private void sendSms(){
        SmsManager smsManager = (SmsManager) SmsManager.getDefault();
        if (contactNumbers != null && meetingTimes != null) {
            for (int i = 0; i < meetingTimes.size(); i++) {
                time = meetingTimes.get(i);

                Log.i("SendSMS", "time i loop: " + time);

                text = "Minner om at du har et møte idag klokka " + "\n" +
                        time;

                number = contactNumbers.get(i);

                Log.i("INFO", "tekst og nummer: " + text +
                        " --sendes til: " + number);

                smsManager.sendTextMessage(number, null, text,
                        null, null);
                Log.i("notify","SMS sent!");
            }
        }
    }
}
