package com.example.meetme;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MeetingsActivity extends AppCompatActivity implements MeetingAdapter.OnMeetingListener {

    private static final int REQUEST_CODE_MEETING = 1;
    private static final int REQUEST_CODE_MEETING_UPDATE = 2; // Viktig at denne ikke er samme som REQUEST_CODE
    private static final int PERMISSION_SEND_SMS = 123;

    RecyclerView recyclerView;
    MeetMeViewModel meetMeViewModel;
    List<Meeting> allMeetings;
    List<MeetingParti> allMeetingPartis;
    List<Contact> allContacts;
    ArrayList<Contact> meetingPartiContacts;
    Meeting savedMeeting;
    Meeting updatedMeeting;
    String updateOrSaveMeetingParties;
    String time;
    String number;
    ArrayList<Meeting> meetingList = new ArrayList<>();
    ArrayList<MeetingParti> meetingPartiList = new ArrayList<>();
    ArrayList<Contact> contactList = new ArrayList<>();

    ArrayList<String> contactNumbers = new ArrayList<>();
    ArrayList<String> meetingTimes = new ArrayList<>();
    ArrayList<Date> dateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.meetme_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.meetings);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.meetings:
                    return true;

                case R.id.contacts:
                    startActivity(new Intent(getApplicationContext(),
                            ContactsActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;

                case R.id.settings:
                    startActivity(new Intent(getApplicationContext(),
                            SettingsActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }
            return false;
        });

        setButtonsAndViews();


        recyclerView = findViewById(R.id.meeting_recyclerview);
        final MeetingAdapter adapter = new MeetingAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Meeting myMeeting = adapter.getMeetingPosition(position);
                Log.i("DELETE", "onSwiped delete: " + myMeeting);
                // Delete the word
                meetMeViewModel.deleteMeetingPartiByMeetingId(myMeeting.getMeetingId());
                meetMeViewModel.delete(myMeeting);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        // sharedPrefs
        SharedPreferences sharedPrefs = getSharedPreferences("list-prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        meetMeViewModel = new ViewModelProvider(this).get(MeetMeViewModel.class);
        meetMeViewModel.getAllMeetings().observe(this, new Observer<List<Meeting>>() {
            @Override
            public void onChanged(List<Meeting> meetings) {
                adapter.setMeeting(meetings);
                allMeetings = meetings;
                String jsonMeetingList = gson.toJson(allMeetings);
                editor.putString("meetingList", jsonMeetingList);
                editor.apply();

                if (allMeetings != null) {
                    // finner siste møtet som er lagt inn
                    for (int i = 0; i < allMeetings.size(); i++) {
                        if (i == allMeetings.size() - 1) {
                            savedMeeting = allMeetings.get(i);
                        }
                    }
                    // TODO: Dette er her kun for debugging.. slett før levering
                    for (Meeting meeting : allMeetings) {
                        System.out.println("MøterNavn: " + meeting.getMeetingName() + "\n"
                                + "MøteID: " + meeting.getMeetingId() + "\n"
                                + "MøteDato: " + meeting.getDate() + "\n"
                                + "StartTid - SluttTid: " + meeting.getStartTime() + " - "
                                + meeting.getEndTime() + "\n" +
                                "MøteLokasjon: " + meeting.getMeetingLocation() + "\n"
                                + "MøteType: " + meeting.getMeetingType()
                                + "\n--------------------------");
                    }
                    if (meetingPartiContacts != null && updateOrSaveMeetingParties != null) {
                        if (updateOrSaveMeetingParties.equals("save")) {
                            saveMeetingParties();
                        } else {
                            updateMeetingParties();
                        }
                    }
                }
            }
        });

        meetMeViewModel.getAllMeetingParties().observe(this, new Observer<List<MeetingParti>>() {
            @Override
            public void onChanged(List<MeetingParti> meetingPartis) {
                allMeetingPartis = meetingPartis;
                String jsonMeetingPartiList = gson.toJson(allMeetingPartis);
                editor.putString("meetingPartiList", jsonMeetingPartiList);
                editor.apply();

                // Denne kan også bare slettes altså, lagde den for å se at meetingparties inneholder riktige objekter
                if (allMeetingPartis != null) {
                    System.out.println("Informasjon om Meetingparties: \n" + "------------------------------");
                    for (MeetingParti meetingParties : allMeetingPartis) {
                        System.out.println("MøteID: " + meetingParties.getMeetingId() + "\n"
                                + "ContactID: " + meetingParties.getContactId()
                                + "\n--------------------------");
                    }
                }
            }
        });

        meetMeViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                allContacts = contacts;
                String jsonContactiList = gson.toJson(allContacts);
                editor.putString("contactList", jsonContactiList);
                editor.apply();
            }
        });

        loadSettings();
        checkMeeting();
    }

    private void checkMeeting() {
        // TODO: bare for å se hva calendar returnerer
        Calendar now = Calendar.getInstance();
        Log.i("CALENDAR", "checkMeeting: " + now);
        startNotification();
    }

    private void startNotification() {
        try {
            SharedPreferences listPrefs = getSharedPreferences("list-prefs", MODE_PRIVATE);
            Gson gson = new Gson();
            // Meeting
            String responseMeeting = listPrefs.getString("meetingList", null);
            Type meetingType = new TypeToken<ArrayList<Meeting>>() {
            }.getType();
            meetingList = gson.fromJson(responseMeeting, meetingType);
        } catch (Exception e){
            Log.d("error", "Feil med lagring til sharedPrefs ");
        }

        SharedPreferences notifyPrefs = getSharedPreferences("dates", MODE_PRIVATE);
        SharedPreferences.Editor editor = notifyPrefs.edit();
        Gson gson = new Gson();

        if (meetingList != null) {
            // TODO: list of dates
            for (Meeting meeting : meetingList){
                try {
                    Date date = new SimpleDateFormat("MM / dd / yyyy").parse(meeting.getDate());
                    dateList.add(date);
                } catch (ParseException e){
                    Log.e("Exception", "startNotification exception: ", e);
                }
            }
            String jsonDateList = gson.toJson(dateList);
            editor.putString("dateList", jsonDateList);
            editor.apply();
        }

        Intent intent = new Intent();
        intent.setAction("com.example.meetme.secondbroadcast");
        sendBroadcast(intent);
    }

    public void loadSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        time = sharedPrefs.getString("alarmTime", "06:00");
        Log.d("SharedPrefs", "loadSettings: " + time);
        System.out.println("time fra sharedprefs: " + time);
        boolean state = sharedPrefs.getBoolean("message", false);
        if (state){
            System.out.println("CONTACTS: Dette er en test for å sjekke at varsling er på");
            System.out.println("CONTACTS: dette er tidspunkt: " + time);
            // startService();
            checkPermission();
        } else {
            System.out.println("CONTACTS: Dette er en test for å sjekke at varsling er av");
            stopService();
        }
    }

    private void checkPermission() {
        // TODO: ask for permission
        // TODO: mulig getApplication må byttes ut med this her
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.SEND_SMS) != PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        } else {
            System.out.println("bruker har allerede gitt tilatelse");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    System.out.println("GRANTED");
                    startService();
                } else {
                    // permission denied
                    System.out.println("Permission denied");
                }
                return;
            }
        }
    }

    private void startService() {
        try {
            SharedPreferences listPrefs = getSharedPreferences("list-prefs", MODE_PRIVATE);
            Gson gson = new Gson();
            // Meeting
            String responseMeeting = listPrefs.getString("meetingList", null);
            Type meetingType = new TypeToken<ArrayList<Meeting>>() {
            }.getType();
            meetingList = gson.fromJson(responseMeeting, meetingType);

            // MeetingParti
            String responsParti = listPrefs.getString("meetingPartiList", null);
            Type partiType = new TypeToken<ArrayList<MeetingParti>>() {}.getType();
            meetingPartiList = gson.fromJson(responsParti, partiType);

            // Contact
            String responsContact = listPrefs.getString("contactList", null);
            Type contactType = new TypeToken<ArrayList<Contact>>() {}.getType();
            contactList = gson.fromJson(responsContact, contactType);

        }catch (Exception e){
            System.out.println("Exception loading from sharedPrefs" + e);
        }

        if (contactList != null || meetingList != null || meetingPartiList != null) {

            for (MeetingParti meetingPartiObj : meetingPartiList) { // løper gjennom meetingParti
                System.out.println("Kontakt ID for meetingParti: " + meetingPartiObj.getContactId());
                for (Contact contactObj : contactList) { // løper gjennom kontakter
                    System.out.println("Kontakter ID: " + contactObj.getContactId() + "\n"
                            + contactObj.getContactNumber());
                    if (contactObj.getContactId() == meetingPartiObj.getContactId()) { // Hvis id matcher
                        number = contactObj.getContactNumber();
                        contactNumbers.add(number);
                    }
                }
                for (Meeting meetingObj : meetingList) {
                    System.out.println("MøteID " + meetingObj.getMeetingId() + " " + meetingObj.getMeetingName());
                    if (meetingObj.getMeetingId() == meetingPartiObj.getMeetingId()) {
                        time = meetingObj.getDate() + " " +meetingObj.getStartTime();
                        meetingTimes.add(time);
                    }
                }
            }
        } else {
            Log.d("StartService", "en av listene er tomme ");
        }

        // TODO: SMS ting under her
        System.out.println("Her kommer telefonnummer og tidspunkter for mæter: \n" +
                "---------------------------");
        for (int i = 0; i < contactNumbers.size(); i++){
            System.out.println(contactNumbers.get(i));
            System.out.println("----");
        }
        System.out.println("--------tider under-----");
        for (int f = 0; f <meetingTimes.size(); f++){
            System.out.println(meetingTimes.get(f));
            System.out.println("----");
        }
        SharedPreferences sharedSMSPrefs = getSharedPreferences("SMSInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedSMSPrefs.edit();
        Gson gson = new Gson();
        String jsonContactiNumbers = gson.toJson(contactNumbers);
        editor.putString("contactNumberList", jsonContactiNumbers);

        String jsonMeetingTimes = gson.toJson(meetingTimes);
        editor.putString("meetingTimeList", jsonMeetingTimes);
        editor.apply();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("time", time);
        intent.putExtras(bundle);
        intent.setAction("com.example.meetme.mybroadcast");
        sendBroadcast(intent);
    }

    private void stopService() {
        Intent i = new Intent(this, MySMSService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
            Log.d("Service", "stopService: ");
        }
    }

    // lagring og oppdatering av møte
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Contact>>() {}.getType();
        String gsonString = data.getStringExtra("MeetingPartiList");
        meetingPartiContacts = gson.fromJson(gsonString, type);

        if (requestCode == REQUEST_CODE_MEETING && resultCode == RESULT_OK) {
            updateOrSaveMeetingParties = "save";
            Meeting newMeeting = new Meeting(data.getStringExtra("meeting_name"),
                    data.getStringExtra("meeting_location"),
                    data.getStringExtra("meeting_type"),
                    data.getStringExtra("meeting_start_time"),
                    data.getStringExtra("meeting_end_time"),
                    data.getStringExtra("meeting_date"));
            meetMeViewModel.insert(newMeeting);

        } else if (requestCode == REQUEST_CODE_MEETING_UPDATE && resultCode == RESULT_OK) {
            updateOrSaveMeetingParties = "update";

            int id = data.getIntExtra("meetingId", -1);
            if (id == -1) {
                Log.d("Update", "update failed: ");
                return;
            }
            Meeting meeting = new Meeting(data.getStringExtra("meetingName"),
                    data.getStringExtra("meetingLocation"),
                    data.getStringExtra("meetingType"),
                    data.getStringExtra("meetingStartTime"),
                    data.getStringExtra("meetingEndTime"),
                    data.getStringExtra("meetingDate"));
            meeting.setMeetingId(id);

            updatedMeeting = meeting;
            meetMeViewModel.updateMeeting(meeting);
            Log.i("meeting", "Møte oppdatert: ");
        } else {
            Log.d("SAVE", "no data saved: ");
        }
    }

    private void saveMeetingParties() {
        for (Contact contact : meetingPartiContacts) {
            int contactId = contact.getContactId();
            MeetingParti meetingParti = new MeetingParti(savedMeeting.getMeetingId(), contactId);
            meetMeViewModel.insert(meetingParti);
        }
    }

    private void updateMeetingParties() {
        System.out.println("Møtet som oppdatered er: " + updatedMeeting.getMeetingName() +
                ". med ID: " + updatedMeeting.getMeetingId());
        meetMeViewModel.deleteMeetingPartiByMeetingId(updatedMeeting.getMeetingId());
        for (Contact contact : meetingPartiContacts) {
            int contactId = contact.getContactId();
            MeetingParti meetingParti = new MeetingParti(updatedMeeting.getMeetingId(), contactId);
            meetMeViewModel.insert(meetingParti);
        }
    }

    private void setButtonsAndViews() {
        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.fabMeeting);
        btnAdd.setOnClickListener(v -> {
            Intent i = new Intent(MeetingsActivity.this, NewMeetingActivity.class);
            startActivityForResult(i, REQUEST_CODE_MEETING);
        });
    }

    @Override
    public void onMeetingClick(Meeting meeting) {
        ArrayList<MeetingParti> thisMeeetingsParties = new ArrayList<>();
        ArrayList<String> meetingPartiContactsToUpdate = new ArrayList<>();

        // Finner meetingpartiene som tilhører møte som klikkes på
        if (allMeetingPartis != null) {
            for (MeetingParti meetingParti : allMeetingPartis) {
                if (meetingParti.getMeetingId() == meeting.getMeetingId()) {
                    thisMeeetingsParties.add(meetingParti);
                }
            }

            // Finner navnene til alle contactIDene som er lagret i disse meetingpartiene.
            if (allContacts != null) {
                for (int i = 0; i < thisMeeetingsParties.size(); i ++) {
                    for (int j = 0; j < allContacts.size(); j ++) {
                        if (thisMeeetingsParties.get(i).getContactId() == allContacts.get(j).getContactId()) {
                            meetingPartiContactsToUpdate.add(allContacts.get(j).getContactName());
                        }
                    }
                }
            }
        }

        // Info om møtet som trykkes på sendes til updateMeeting
        Gson gson = new Gson();
        String json = gson.toJson(meetingPartiContactsToUpdate);
        Intent intent = new Intent(MeetingsActivity.this,
                UpdateMeetingActivity.class);
        intent.putExtra("meeting_id", meeting.getMeetingId());
        intent.putExtra("meeting_name", meeting.getMeetingName());
        intent.putExtra("meeting_location", meeting.getMeetingLocation());
        intent.putExtra("meeting_type", meeting.getMeetingType());
        intent.putExtra("meeting_start_time", meeting.getStartTime());
        intent.putExtra("meeting_end_time", meeting.getEndTime());
        intent.putExtra("meeting_date", meeting.getDate());
        intent.putExtra("meeting_parti_list", json);

        startActivityForResult(intent, REQUEST_CODE_MEETING_UPDATE);
    }
}