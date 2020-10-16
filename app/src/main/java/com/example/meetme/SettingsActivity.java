package com.example.meetme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.meetme_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.meetings:
                        startActivity(new Intent(getApplicationContext(),
                                MeetingsActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.contacts:
                        startActivity(new Intent(getApplicationContext(),
                                ContactsActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.settings:
                        return true;
                }
                return false;
            }
        });

    }


}