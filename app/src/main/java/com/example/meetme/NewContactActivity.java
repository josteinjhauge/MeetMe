package com.example.meetme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.DialogFragment;

public class NewContactActivity extends AppCompatActivity{
    private static final String DIALOG2 = "Dialog2";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_NUMBER = "contact_number";

    EditText contact_name;
    EditText contact_number;
    TextView name_error;
    TextView number_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setButtonsAndViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
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
      }
        return super.onOptionsItemSelected(item);
    }

    private void setButtonsAndViews() {
        // Views
        contact_name = findViewById(R.id.contact_name);
        contact_number = findViewById(R.id.contact_number);
        name_error = findViewById(R.id.name_error);
        number_error = findViewById(R.id.number_error);
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
        if (contactName.equals("")) {
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
            saveDialog();
        }
    }

    private void saveDialog() {
        DialogFragment saveDialogContactFragment = new SaveDialogContactFragment();
        saveDialogContactFragment.show(getSupportFragmentManager(), DIALOG2);
        Log.i("TAG", "onClick: ");

        saveContact();
    }

    public void saveContact() {
        Intent replyIntent = new Intent();
        String name = contact_name.getText().toString();
        String number = contact_number.getText().toString();

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(number)) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            replyIntent.putExtra(CONTACT_NAME, name);
            replyIntent.putExtra(CONTACT_NUMBER, number);
            setResult(RESULT_OK, replyIntent);
        }
    }
}