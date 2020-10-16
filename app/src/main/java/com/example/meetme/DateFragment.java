package com.example.meetme;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    @Override
    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        String dateType = this.getArguments().getString("date_type");
        String date = String.format("%d / %d / %d", mm+1, dd, yy);
        Button btn_date = (Button)getActivity().findViewById(R.id.btn_date);
        Button btn_date_update = (Button)getActivity().findViewById(R.id.btn_date_update);
        try {
            if(dateType.equals("date")) {
                btn_date.setText(date);
            } else {
                btn_date_update.setText(date);
            }
        } catch (Exception e) {
            Log.d("OnDateSet", "exception in Datefragment.onDateSet: " + e);
        }
    }
}