package com.example.meetme;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class SettingsFragment extends PreferenceFragmentCompat  {
    private static final int PERMISSION_SEND_SMS = 123;
    SwitchPreference switchPreference;
    EditTextPreference editTextPreference;
    Context context;

    public SettingsFragment() {
        // Required empty public constructor
    }

   @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey){
       setPreferencesFromResource(R.xml.preferences, rootKey);
        switchPreference = (SwitchPreference) findPreference("message");
        editTextPreference = (EditTextPreference) findPreference("alarmTime");

       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
       boolean state = sharedPreferences.getBoolean("message", false);
       if (state){
           checkPermission();
           // switchPreference.setIcon(R.drawable.ic_notifications_on);
           Toast.makeText(getActivity(), "SMS PÅ", Toast.LENGTH_SHORT).show();
           System.out.println("SMS PÅ");

       } else {
           switchPreference.setChecked(false);
           // switchPreference.setIcon(R.drawable.ic_notifications_off);
           Toast.makeText(getActivity(), "SMS AV", Toast.LENGTH_SHORT).show();
           System.out.println("SMS Av");
       }
   }

    private void checkPermission() {
        // TODO: ask for permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
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
                } else {
                    // permission denied
                }
                return;
            }
        }
    }
}