package com.example.meetme;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getContext()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int hourOfDayLength = String.valueOf(hourOfDay).length();
        int minuteLength = String.valueOf(minute).length();
        String hourOfDayString;
        String minuteString;

        if (hourOfDayLength == 1) {
            hourOfDayString = "0" + hourOfDay;
        } else {
            hourOfDayString = String.valueOf(hourOfDay);
        }

        if (minuteLength == 1) {
            minuteString = "0" + minute;
        } else {
            minuteString = String.valueOf(minute);
        }

        String timeType = this.getArguments().getString("time_type");
        String time = String.format("%s:%s ", hourOfDayString, minuteString);
        Button btn_time_start = (Button) getActivity().findViewById(R.id.btn_time_start);
        Button btn_time_end = (Button)getActivity().findViewById(R.id.btn_time_end);
        Button btn_time_start_update = (Button) getActivity().findViewById(R.id.btn_time_start_update);
        Button btn_time_end_update  = (Button)getActivity().findViewById(R.id.btn_time_end_update);


        try {
            if (timeType.equals("startTime")) {
                btn_time_start.setText(time);
            } else if (timeType.equals("endTime")) {
                btn_time_end.setText(time);
            } else if (timeType.equals("startTime_update")) {
                btn_time_start_update.setText(time);
            } else {
                btn_time_end_update.setText(time);
            }
        } catch (Exception e) {
            Log.d("OnTimeSet", "exception in Timefragment.onTimeSet: " + e);
        }
    }
}
