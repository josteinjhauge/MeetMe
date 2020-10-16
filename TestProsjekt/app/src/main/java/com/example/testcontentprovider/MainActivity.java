package com.example.testcontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String AUTHORITY = "com.example.meetme";
    public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY + "/contact_table");
    EditText contactID;
    TextView fetchedContact;
    TextView fetchedContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactID = (EditText) findViewById(R.id.contactID);
        fetchedContact = (TextView) findViewById(R.id.fetchedContact);
        fetchedContacts = (TextView) findViewById(R.id.fetchedContacts);
    }

    public void showContact(View v){
        String id = contactID.getText().toString();
        Uri uri_wID = Uri.parse(CONTENT_URI + "/" + id);
        String out = "";
        Cursor cur = getContentResolver().query(uri_wID, null, null, null, null );

        if (cur.moveToFirst()) {
            do {
                out += (cur.getString(1)) + "\r\n";
            }
            while (cur.moveToNext());
            cur.close();
            fetchedContact.setText(out);
        }
    }

    public void showAllContacts(View v) {
        String out = "";
        Cursor cur = getContentResolver().query(CONTENT_URI, null, null, null, null);

        if (cur.moveToFirst()) {
            do {
                out += (cur.getString(1)) + "\r\n";
            }
            while (cur.moveToNext());
            cur.close();
            fetchedContacts.setText(out);
        }
    }
}