package com.example.meetme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    // KEYS
    private static final String MEETING_NAME = "meeting_name";
    private static final String MEETING_LOCATION = "meeting_location";
    private static final String MEETING_TYPE = "meeting_type";
    private static final String MEETING_START_TIME = "meeting_start_time";
    private static final String MEETING_END_TIME = "meeting_end_time";
    private static final String MEETING_DATE = "meeting_date";
    private static final String MEETINGPARTILIST = "MeetingPartiList";

    public static final String MEETINGPARTI_ARRAY = "meetingParti_array";
    public static final int MEETINGPARTI_RESULT = 1;

    // TextFields
    private EditText meeting_name;
    private EditText meeting_location;
    private EditText meeting_type;
    private TextView meetingName_error;
    private TextView location_error;
    private TextView type_error;
    private TextView startTime_error;
    private TextView endTime_error;
    private TextView date_error;
    private ListView meetingParties_listview;

    // Buttons
    private Button btn_time_start;
    private Button btn_time_end;
    private Button btn_date;

    // Strings
    String DATEPICKER = "Datepicker";
    String STARTTIME = "Starttime";
    String ENDTIME = "Endtime";
    String DIALOG = "Dialog";

    ArrayList<Contact> meetingPartiContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setButtonsAndViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meeting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.btn_meeting_parti:
                startMultiSelect();
                return true;
            case R.id.save_btn:
                checkInput();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setButtonsAndViews() {
        // Views
        meeting_name = (EditText)findViewById(R.id.meeting_name);
        meeting_location = (EditText)findViewById(R.id.location);
        meeting_type = (EditText)findViewById(R.id.type);
        meetingName_error = (TextView)findViewById(R.id.meetingName_error);
        location_error = (TextView)findViewById(R.id.location_error);
        type_error = (TextView)findViewById(R.id.type_error);
        startTime_error = (TextView)findViewById(R.id.startTime_error);
        endTime_error = (TextView)findViewById(R.id.endTime_error);
        date_error = (TextView)findViewById(R.id.date_error);
        meetingParties_listview = (ListView)findViewById(R.id.meetingParties_listview);

        // Buttons
        btn_time_start = (Button)findViewById(R.id.btn_time_start);
        btn_time_end = (Button)findViewById(R.id.btn_time_end);
        btn_date = (Button)findViewById(R.id.btn_date);

        btn_time_start.setOnClickListener(this);
        btn_time_end.setOnClickListener(this);
        btn_date.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        if (v == btn_date) {
            String dateType = "date";
            DialogFragment dateFragment = new DateFragment();
            bundle.putString("date_type", dateType);
            dateFragment.setArguments(bundle);
            dateFragment.show(getSupportFragmentManager(), DATEPICKER);
        }

        if (v == btn_time_start) {
            String timeType = "startTime";
            DialogFragment startTimeFragment = new TimeFragment();
            bundle.putString("time_type", timeType);
            startTimeFragment.setArguments(bundle);
            startTimeFragment.show(getSupportFragmentManager(), STARTTIME);
            Log.i("TAG", "onClick: " + timeType);
        }

        if (v == btn_time_end) {
            String timeType = "endTime";
            DialogFragment endTimeFragment = new TimeFragment();
            bundle.putString("time_type", timeType);
            endTimeFragment.setArguments(bundle);
            endTimeFragment.show(getSupportFragmentManager(), ENDTIME);
            Log.i("TAG", "onClick: " + timeType);
        }
    }

    public void startMultiSelect(){
        Intent intent = new Intent(this, MeetingPartiActivity.class);
        startActivityForResult(intent, MEETINGPARTI_RESULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Contact>>() {}.getType();

        if (requestCode == MEETINGPARTI_RESULT && resultCode == RESULT_OK) {
            String gsonString = data.getStringExtra(MEETINGPARTI_ARRAY);
            meetingPartiContacts = gson.fromJson(gsonString, type);
            ArrayList<String> partiNames = new ArrayList<>();
            for (Contact contact : meetingPartiContacts) {
                partiNames.add(contact.getContactName());
            }
            if (meetingPartiContacts != null) {
                ArrayAdapter adapter = new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, partiNames); // TODO: denne bør styles
                meetingParties_listview.setAdapter(adapter);
            }
        } else{
            Toast.makeText(
                    getApplicationContext(),
                    "No Data saved...",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void checkInput() {
        // Regex
        String regex_name = "^[-.,/!?0-9a-zA-ZæøåÆØÅ ]{1,30}$";
        String regex_location = "^[-.,/0-9a-zA-ZæøåÆØÅ ]{1,30}$";
        String regex_type = "^[-.,/0-9a-zA-ZæøåÆØÅ ]{1,30}$";

        // Input
        String meetingName = meeting_name.getText().toString();
        String location = meeting_location.getText().toString();
        String type = meeting_type.getText().toString();
        String startTime = btn_time_start.getText().toString();
        String endTime = btn_time_end.getText().toString();
        String date = btn_date.getText().toString();

        // Validerings status
        boolean meetingNameOK;
        boolean locationOK;
        boolean typeOK;
        boolean startTimeOK;
        boolean endTimeOK;
        boolean dateOK;
        boolean timeOK;

        // meetingName check
        if (meetingName.equals("")) {
            meetingName_error.setText(getResources().getString(R.string.input_empty_val));
            meetingNameOK = false;
        } else if (!meetingName.matches(regex_name)) {
            meetingName_error.setText(getResources().getString(R.string.input_name_val));
            meetingNameOK = false;
        } else {
            meetingName_error.setText("");
            meetingNameOK = true;
        }

        // location check
        if (location.equals("")) {
            location_error.setText(getResources().getString(R.string.input_empty_val));
            locationOK = false;
        } else if (!location.matches(regex_location)) {
            location_error.setText(getResources().getString(R.string.input_location_val));
            locationOK = false;
        } else {
            location_error.setText("");
            locationOK = true;
        }

        // type check
        if (type.equals("")) {
            type_error.setText(getResources().getString(R.string.input_empty_val));
            typeOK = false;
        } else if (!type.matches(regex_type)) {
            type_error.setText(getResources().getString(R.string.input_type_val));
            typeOK = false;
        } else {
            type_error.setText("");
            typeOK = true;
        }

        // startTime check
        if (startTime.equals("Start tid")) {
            startTime_error.setText(getResources().getString(R.string.input_empty_val));
            startTimeOK = false;
        } else {
            startTime_error.setText("");
            startTimeOK = true;
        }

        // endTime check
        if (endTime.equals("Slutt tid")) {
            endTime_error.setText(getResources().getString(R.string.input_empty_val));
            endTimeOK = false;
        } else {
            endTime_error.setText("");
            endTimeOK = true;
        }

        // date check
        if (date.equals("Velg dato")) {
            date_error.setText(getResources().getString(R.string.input_empty_val));
            dateOK = false;
        } else {
            date_error.setText("");
            dateOK = true;
        }

        // endDate after startDate check
        if (startTimeOK && endTimeOK) {
            try {
                Date startTimeDate = new SimpleDateFormat("HH:mm").parse(startTime);
                Date endTimeDate = new SimpleDateFormat("HH:mm").parse(endTime);

                if((startTimeDate).after(endTimeDate)){
                    endTime_error.setText(getResources().getString(R.string.wrong_time_warning));
                    timeOK = false;
                } else {
                    endTime_error.setText("");
                    timeOK = true;
                }
            } catch (ParseException e) {
                timeOK = false;
                e.printStackTrace();
            }
        } else {
            timeOK = false;
        }

        if (meetingNameOK && locationOK && typeOK && startTimeOK && endTimeOK && dateOK && timeOK) {
            saveDialog();
        }
    }

    private void saveDialog() {
        DialogFragment saveDialogFragment = new SaveDialogFragment();
        saveDialogFragment.show(getSupportFragmentManager(), DIALOG);
        if (meetingPartiContacts != null){
            Bundle args = new Bundle();
            args.putParcelableArrayList("partiList", meetingPartiContacts);
            saveDialogFragment.setArguments(args);
        }
        saveMeeting();
    }

    private void saveMeeting() {
        Intent replyIntent = new Intent();

        String meetingName = meeting_name.getText().toString();
        String meetingLocation = meeting_location.getText().toString();
        String meetingType = meeting_type.getText().toString();
        String startTime = btn_time_start.getText().toString();
        String endTime = btn_time_end.getText().toString();
        String date = btn_date.getText().toString();

        Gson gson = new Gson();
        String json = gson.toJson(meetingPartiContacts);

        if (TextUtils.isEmpty(meetingName) && TextUtils.isEmpty(meetingLocation)
            && TextUtils.isEmpty(meetingType) && TextUtils.isEmpty(startTime)
                && TextUtils.isEmpty(endTime) && TextUtils.isEmpty(date)) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            replyIntent.putExtra(MEETING_NAME, meetingName);
            replyIntent.putExtra(MEETING_LOCATION, meetingLocation);
            replyIntent.putExtra(MEETING_TYPE, meetingType);
            replyIntent.putExtra(MEETING_START_TIME, startTime);
            replyIntent.putExtra(MEETING_END_TIME, endTime);
            replyIntent.putExtra(MEETING_DATE, date);
            replyIntent.putExtra(MEETINGPARTILIST, json);
            setResult(RESULT_OK, replyIntent);
        }
    }
}