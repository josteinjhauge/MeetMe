package com.example.meetme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static MeetMeDatabase meetMeDatabase;
    private List<Contact> contacts_list;
    private MeetMeViewModel meetMeViewModel;
    // private MeetMeListAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meetMeDatabase = Room.databaseBuilder(getApplicationContext(), MeetMeDatabase.class,
                "meetme_database").build();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.meetings);
        Intent i = new Intent(MainActivity.this, MeetingsActivity.class);
        startActivity(i);
        finish();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.meetings:
                        startActivity(new Intent(getApplicationContext(),
                                MeetingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.contacts:
                        startActivity(new Intent(getApplicationContext(),
                                ContactsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),
                                SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });


    }



}