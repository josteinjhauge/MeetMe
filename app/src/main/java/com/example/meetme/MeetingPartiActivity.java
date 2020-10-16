package com.example.meetme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;

public class MeetingPartiActivity extends AppCompatActivity {

    public static final String MEETINGPARTI_NAME = "meetingPart_name";
    public static final String MEETINGPARTI_NUMBER = "meetingPart_number";
    public static final String MEETINGPARTI_ARRAY = "meetingParti_array";

    RecyclerView recyclerView;
    MeetingPartiAdapter meetingPartiAdapter;
    MeetMeViewModel meetMeViewModel;
    ArrayList<Contact> selectedContacts;
    List<Meeting> allMeetings;
    List<MeetingParti> allMeetingPartis;
    List<Contact> allContacts;

    List<Contact> contactsHasMeetings = new ArrayList<>();
    List<Contact> freeContacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetingparties);

        recyclerView = findViewById(R.id.meetingparties_recyclerview);
        final MeetingPartiAdapter adapter = new MeetingPartiAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        meetingPartiAdapter = adapter;

        // Viewmodel
        meetMeViewModel = new ViewModelProvider(this).get(MeetMeViewModel.class);
        meetMeViewModel.getAllMeetings().observe(this, meetings -> allMeetings = meetings);
        meetMeViewModel.getAllMeetingParties().observe(this, meetingpartis -> allMeetingPartis = meetingpartis);
        meetMeViewModel.getAllContacts().observe(this, contacts -> adapter.setContact(contacts));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.save_btn:
                addPartis();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addPartis() {
        Intent data = new Intent();
        Gson gson = new Gson();
        selectedContacts = meetingPartiAdapter.getSelected();
        if (selectedContacts.size() > 0) {
            String json = gson.toJson(selectedContacts);
            data.putExtra(MEETINGPARTI_ARRAY, json);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}