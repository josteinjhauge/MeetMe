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

public class UpdateMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MEETING_UPDATE_NAME = "meetingName";
    private static final String MEETING_UPDATE_LOCATION = "meetingLocation";
    private static final String MEETING_UPDATE_TYPE = "meetingType";
    private static final String MEETING_UPDATE_START_TIME = "meetingStartTime";
    private static final String MEETING_UPDATE_END_TIME = "meetingEndTime";
    private static final String MEETING_UPDATE_DATE = "meetingDate";
    private static final String MEETING_UPDATE_ID = "meetingId";
    private static final String MEETINGPARTILIST = "MeetingPartiList";

    private static final int MEETINGPARTI_RESULT_UPDATE = 1;

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
    private ListView meetingParties_listview_update;

    // Buttons
    private Button btn_time_start;
    private Button btn_time_end;
    private Button btn_date;

    // Strings
    String DATEPICKER = "Datepicker";
    String STARTTIME = "Starttime";
    String ENDTIME = "Endtime";

    Meeting currentMeeting;
    MeetMeViewModel meetMeViewModel;

    ArrayList<Contact> updateMeetingPartiContacts;
    ArrayList<String> currentMeetingPartiContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meeting);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setButtonsAndViews();

        Intent intent = getIntent();
        Gson gson = new Gson();
        Type typeG = new TypeToken<ArrayList<String>>() {}.getType();
        if (intent.hasExtra("meeting_id")) {

            int meetingId = intent.getIntExtra("meeting_id", -1);
            String name = intent.getStringExtra("meeting_name");
            String location = intent.getStringExtra("meeting_location");
            String type = intent.getStringExtra("meeting_type");
            String startTime = intent.getStringExtra("meeting_start_time");
            String endTime = intent.getStringExtra("meeting_end_time");
            String date = intent.getStringExtra("meeting_date");
            String gsonString = intent.getStringExtra("meeting_parti_list");

            meeting_name.setText(name);
            meeting_location.setText(location);
            meeting_type.setText(type);
            btn_time_start.setText(startTime);
            btn_time_end.setText(endTime);
            btn_date.setText(date);

            currentMeetingPartiContacts = gson.fromJson(gsonString, typeG);

            if (currentMeetingPartiContacts != null) {
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, currentMeetingPartiContacts);
                meetingParties_listview_update.setAdapter(adapter);
            } // TODO: bør vel lagres sammen med møte

            currentMeeting = new Meeting(meetingId, name, location, type, startTime,
                    endTime, date);
        }
        meetMeViewModel = new ViewModelProvider(this).get(MeetMeViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_meeting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.save_btn:
                checkInput();
                return true;
            case R.id.delete_btn:
                meetMeViewModel.deleteMeetingPartiByMeetingId(currentMeeting.getMeetingId());
                meetMeViewModel.delete(currentMeeting);
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.btn_meeting_parti:
                startMultiSelect();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setButtonsAndViews() {
        // Views
        meeting_name = (EditText) findViewById(R.id.meeting_name_update);
        meeting_location = (EditText) findViewById(R.id.location_update);
        meeting_type = (EditText) findViewById(R.id.type_update);
        meetingName_error = (TextView) findViewById(R.id.update_meetingName_error);
        location_error = (TextView) findViewById(R.id.update_location_error);
        type_error = (TextView) findViewById(R.id.update_type_error);
        startTime_error = (TextView) findViewById(R.id.update_startTime_error);
        endTime_error = (TextView) findViewById(R.id.update_endTime_error);
        date_error = (TextView) findViewById(R.id.update_date_error);
        meetingParties_listview_update = (ListView) findViewById(R.id.meetingParties_listview_update);

        // Buttons
        btn_time_start = (Button) findViewById(R.id.btn_time_start_update);
        btn_time_end = (Button) findViewById(R.id.btn_time_end_update);
        btn_date = (Button) findViewById(R.id.btn_date_update);

        btn_time_start.setOnClickListener(this);
        btn_time_end.setOnClickListener(this);
        btn_date.setOnClickListener(this);
    }

    public void startMultiSelect() {
        Intent intent = new Intent(this, MeetingPartiActivity.class);
        startActivityForResult(intent, MEETINGPARTI_RESULT_UPDATE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Contact>>() {}.getType();

        if (requestCode == MEETINGPARTI_RESULT_UPDATE && resultCode == RESULT_OK) {
            String gsonString = data.getStringExtra("meetingParti_array");
            updateMeetingPartiContacts = gson.fromJson(gsonString, type);
            ArrayList<String> partiNames = new ArrayList<>();
            for (Contact contact : updateMeetingPartiContacts) {
                partiNames.add(contact.getContactName());
            }
            if (updateMeetingPartiContacts != null) {
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, partiNames);
                meetingParties_listview_update.setAdapter(adapter);
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "No Data saved...",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        if (v == btn_date) {
            String dateType = "date_update";
            DialogFragment dateFragment = new DateFragment();
            bundle.putString("date_type", dateType);
            dateFragment.setArguments(bundle);
            dateFragment.show(getSupportFragmentManager(), DATEPICKER);
        }

        if (v == btn_time_start) {
            String timeType = "startTime_update";
            DialogFragment startTimeFragment = new TimeFragment();
            bundle.putString("time_type", timeType);
            startTimeFragment.setArguments(bundle);
            startTimeFragment.show(getSupportFragmentManager(), STARTTIME);
            Log.i("TAG", "onClick: " + timeType);
        }

        if (v == btn_time_end) {
            String timeType = "endTime_update";
            DialogFragment endTimeFragment = new TimeFragment();
            bundle.putString("time_type", timeType);
            endTimeFragment.setArguments(bundle);
            endTimeFragment.show(getSupportFragmentManager(), ENDTIME);
            Log.i("TAG", "onClick: " + timeType);
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

                if ((startTimeDate).after(endTimeDate)) {
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
            updateMeeting();
        }
    }

    public void updateMeeting() {
        Intent data = new Intent();

        String meetingName = meeting_name.getText().toString();
        String meetingLocation = meeting_location.getText().toString();
        String meetingType = meeting_type.getText().toString();
        String startTime = btn_time_start.getText().toString();
        String endTime = btn_time_end.getText().toString();
        String date = btn_date.getText().toString();

        Gson gson = new Gson();
        String json = gson.toJson(updateMeetingPartiContacts);

        if (TextUtils.isEmpty(meetingName) && TextUtils.isEmpty(meetingLocation)
                && TextUtils.isEmpty(meetingType) && TextUtils.isEmpty(startTime)
                && TextUtils.isEmpty(endTime) && TextUtils.isEmpty(date)) {
            setResult(RESULT_CANCELED, data);
        } else {
            data.putExtra(MEETING_UPDATE_NAME, meetingName);
            data.putExtra(MEETING_UPDATE_LOCATION, meetingLocation);
            data.putExtra(MEETING_UPDATE_TYPE, meetingType);
            data.putExtra(MEETING_UPDATE_START_TIME, startTime);
            data.putExtra(MEETING_UPDATE_END_TIME, endTime);
            data.putExtra(MEETING_UPDATE_DATE, date);
            data.putExtra(MEETINGPARTILIST, json);

            int id = getIntent().getIntExtra("meeting_id", -1);
            if (id != -1) {
                data.putExtra(MEETING_UPDATE_ID, id);
            }
            setResult(RESULT_OK, data);
            finish();
        }
    }
}