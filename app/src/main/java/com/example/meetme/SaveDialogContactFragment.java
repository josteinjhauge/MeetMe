package com.example.meetme;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import static android.content.ContentValues.TAG;

public class SaveDialogContactFragment extends DialogFragment {
    EditText contact_name;
    EditText telefonnr;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View DialogView2 = inflater.inflate(R.layout.alert_dialog_contact, null);

        contact_name = (EditText) getActivity().findViewById(R.id.contact_name);
        telefonnr = (EditText) getActivity().findViewById(R.id.contact_number);

        String out2 = "Kontakt navn: " + contact_name.getText().toString() + "\n"
                + "TelefonNr: " + telefonnr.getText().toString() + "\n";
        System.out.println("her kommer out: \n" + out2);

        builder.setView(DialogView2).setTitle(R.string.your_contact)
                .setMessage(out2)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) ->
                        Log.i(TAG, "onCreateDialog: canceled operation"))
                .setPositiveButton(R.string.save, (dialogInterface, i) -> {
                    Activity currentActivity = getActivity();
                    currentActivity.finish();
                });
        return builder.create();

    }
}
