package com.example.meetme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements ContactAdapter.OnContactListener {

    private static final String TAG = "clicked";
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_UPDATE = 2; // Viktig at denne ikke er samme som REQUEST_CODE

    RecyclerView recyclerView;
    MeetMeViewModel meetMeViewModel;
    List<Contact> contactList;
    List<Meeting> meetingList;
    List<MeetingParti> meetingPartiList;
    String time;
    String number;
    ArrayList<String> contactNumbers = new ArrayList<>();
    ArrayList<String> meetingTimes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.meetme_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.contacts);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.meetings:
                    startActivity(new Intent(getApplicationContext(),
                            MeetingsActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;

                case R.id.contacts:
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

        // loadSettings();
        setButtonsAndViews();


        recyclerView = findViewById(R.id.contact_recyclerview);
        final ContactAdapter adapter = new ContactAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Contact myContact = adapter.getContactPosition(position);
                Toast.makeText(ContactsActivity.this, "Deleting " +
                        myContact.getContactName(), Toast.LENGTH_LONG).show();
                System.out.println("Deleting: " + myContact);
                // Delete the word
                meetMeViewModel.deleteMeetingPartiByContactId(myContact.getContactId());
                meetMeViewModel.delete(myContact);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        // sharedPrefs
        SharedPreferences sharedPrefs = getSharedPreferences("list-prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

       // Viewmodel
        meetMeViewModel = new ViewModelProvider(this).get(MeetMeViewModel.class);
        meetMeViewModel.getAllContacts().observe(this, contacts -> {
            adapter.setContact(contacts);
            contactList = contacts;
            String jsonContactList = gson.toJson(contactList);
            editor.putString("contactList", jsonContactList);
            editor.apply();
        });

        meetMeViewModel.getAllMeetings().observe(this, meetings -> {
            meetingList = meetings;
        });

        meetMeViewModel.getAllMeetingParties().observe(this, meetingPartis ->
                meetingPartiList = meetingPartis);

    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Contact contact = new Contact(data.getStringExtra(NewContactActivity.CONTACT_NAME),
                    data.getStringExtra(NewContactActivity.CONTACT_NUMBER));
            meetMeViewModel.insert(contact);

        } else if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK) {
                int id = data.getIntExtra("contactId", -1); // henter basert på key
            System.out.println("her kommer id: " + id);
                if (id == -1) {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = data.getStringExtra("contactName"); // henter basert på key
                String number = data.getStringExtra("contactNumber"); // henter basert på key

                Contact contact = new Contact(name, number);
                contact.setContactId(id); // veldig viktig

                meetMeViewModel.updateContact(contact);
                Toast.makeText(this, "Kontakt oppdatert", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(
                    getApplicationContext(),
                    "No Data saved...",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setButtonsAndViews() {
        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.fabContacts);
        btnAdd.setOnClickListener(v -> {
            Intent i = new Intent(ContactsActivity.this, NewContactActivity.class);
            startActivityForResult(i, REQUEST_CODE);
        });
    }

    @Override
    public void onContactClick(Contact contact) {
        Intent intent = new Intent(ContactsActivity.this,
                UpdateContactActivity.class);
        intent.putExtra("contact_id", contact.getContactId());
        intent.putExtra("contact_name", contact.getContactName());
        intent.putExtra("contact_number", contact.getContactNumber());

        startActivityForResult(intent, REQUEST_CODE_UPDATE);
    }
}