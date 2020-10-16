package com.example.meetme;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SaveDialogFragment extends DialogFragment {
    EditText meeting_name;
    EditText location;
    EditText type;
    Button start_time;
    Button end_time;
    Button in_date;

    ArrayList<Contact> meetingPartis = new ArrayList<>();
    String meetingPartisString = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View DialogView = inflater.inflate(R.layout.alert_dialog, null);

        meeting_name = (EditText)getActivity().findViewById(R.id.meeting_name);
        location = (EditText)getActivity().findViewById(R.id.location);
        type = (EditText)getActivity().findViewById(R.id.type);

        start_time = (Button)getActivity().findViewById(R.id.btn_time_start);
        end_time = (Button)getActivity().findViewById(R.id.btn_time_end);
        in_date = (Button)getActivity().findViewById(R.id.btn_date);

        try {
            Bundle bundle = getArguments();
            meetingPartis = bundle.getParcelableArrayList("partiList");
            for (int i = 0; i < meetingPartis.size(); i ++){
                meetingPartisString += meetingPartis.get(i).getContactName();
                if (i < meetingPartis.size()-2) {
                    meetingPartisString += ", ";
                } else if (i == meetingPartis.size()-2) {
                    meetingPartisString += " og ";
                } else {
                    meetingPartisString += ".";
                }
            }
        } catch (NullPointerException e) {
            Log.d("SaveDialogFragment", "onCreateDialog: nullpointer på meetingPartiArray: " + e);
            meetingPartisString = "ingen valgte deltakere";
        }

        String out = "Møtenavn: " + meeting_name.getText().toString() + "\n"
                + "Sted: " + location.getText().toString() + "\n"
                + "Type møte: "+ type.getText().toString() + "\n"
                + "Dato: " + in_date.getText().toString() + "\n"
                + "Start tid: " + start_time.getText().toString() + "\n"
                + "Slutt tid: " + end_time.getText().toString() + "\n"
                + "Deltagere: " +  meetingPartisString;

        builder.setView(DialogView).setTitle(R.string.your_meeting)
                .setMessage(out)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) ->
                        Log.i(TAG, "onCreateDialog: canceled operation"))
                .setPositiveButton(R.string.save, (dialogInterface, i) -> {
                    Activity currentActivity = getActivity();
                    currentActivity.finish();
                });
        return builder.create();
    }
}
