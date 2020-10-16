package com.example.meetme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

public class UpdateContactActivity extends AppCompatActivity {
    private static final String CONTACT_UPDATE_NAME = "contactName";
    private static final String CONTACT_UPDATE_NUMBER = "contactNumber";
    private static final String CONTACT_ID = "contactId";

    EditText contact_name;
    EditText contact_number;
    TextView name_error;
    TextView number_error;

    MeetMeViewModel meetMeViewModel;
    Contact currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setButtonsAndViews();

        Intent intent = getIntent();
        if (intent.hasExtra("contact_id")) {
            contact_name.setText(intent.getStringExtra("contact_name"));
            contact_number.setText(intent.getStringExtra("contact_number"));

            int contactId = intent.getIntExtra("contact_id", -1);
            String contactName = intent.getStringExtra("contact_name");
            String contactNumber = intent.getStringExtra("contact_number");
            currentContact = new Contact(contactId, contactName, contactNumber);
        }
        meetMeViewModel = new ViewModelProvider(this).get(MeetMeViewModel.class);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.save_btn:
                checkInput();
                return true;
            case R.id.delete_btn:
                meetMeViewModel.deleteMeetingPartiByContactId(currentContact.getContactId());
                meetMeViewModel.delete(currentContact);
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setButtonsAndViews() {
        // Views
        contact_name = findViewById(R.id.contact_name_update);
        contact_number = findViewById(R.id.contact_number_update);
        name_error = findViewById(R.id.name_error_update);
        number_error = findViewById(R.id.number_error_update);

    }

    private void checkInput(){
        // Regex
        String regex_name = "^[-a-zA-ZæøåÆØÅ ]{2,29}$";
        String regex_number = "^[0-9]{8}"; // kun norske telefonnummer

        // Input
        String contactName = contact_name.getText().toString();
        String contactNumber = contact_number.getText().toString();

        // Validering status
        boolean nameOK;
        boolean numberOK;

        // name check
        if (contactName.equals("")){
            name_error.setText(getResources().getString(R.string.input_empty_val));
            nameOK = false;
        } else if (!contactName.matches(regex_name)) {
            name_error.setText(getResources().getString(R.string.input_name_val));
            nameOK = false;
        } else {
            name_error.setText("");
            nameOK = true;
        }

        //number check
        if (contactNumber.equals("")) {
            number_error.setText(getResources().getString(R.string.input_empty_val));
            numberOK = false;
        } else if (!contactNumber.matches(regex_number)){
            number_error.setText(getResources().getString(R.string.input_phone_val));
            numberOK = false;
        } else {
            number_error.setText("");
            numberOK = true;
        }

        if (nameOK && numberOK) {
            updateContact();
        }
    }

    public void updateContact() {
        Intent data = new Intent();
        String name = contact_name.getText().toString();
        String number = contact_number.getText().toString();

        if (name.trim().isEmpty() || number.trim().isEmpty()) {
            return;
        }
        
        data.putExtra(CONTACT_UPDATE_NAME, name);
        data.putExtra(CONTACT_UPDATE_NUMBER, number);
        int id = getIntent().getIntExtra("contact_id", -1);
        if (id != -1) {
            data.putExtra(CONTACT_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();

    }
}