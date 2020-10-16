package com.example.meetme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MeetingPartiAdapter extends RecyclerView.Adapter<MeetingPartiAdapter.MeetingPartiViewHolder> {

    private final LayoutInflater inflater;
    private List<Contact> contactList;

    class MeetingPartiViewHolder extends RecyclerView.ViewHolder {
        private final TextView meetingPartiName;
        private final TextView meetingPartiNumber;

        private ImageView imageView;


        private MeetingPartiViewHolder(View itemView) {
            super(itemView);
            meetingPartiName = itemView.findViewById(R.id.meeting_parti_name);
            meetingPartiNumber = itemView.findViewById(R.id.meeting_parti_number);

            imageView = itemView.findViewById(R.id.check);

        }

        void bind(final Contact contact) {
            imageView.setVisibility(contact.isChecked() ? View.VISIBLE : View.INVISIBLE);
            meetingPartiName.setText(contact.getContactName());
            meetingPartiNumber.setText(contact.getContactNumber());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contact.setChecked(!contact.isChecked());
                    imageView.setVisibility(contact.isChecked() ?
                            View.VISIBLE : View.INVISIBLE);
                }
            });

        }
    }


    MeetingPartiAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MeetingPartiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.meetingparti_recycler_item, parent, false);
        return new MeetingPartiViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeetingPartiViewHolder holder, int position) {
        holder.bind(contactList.get(position));
    }

    void setContact(List<Contact> contacts) {
        contactList = contacts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (contactList != null)
            return contactList.size();
        else return 0;
    }

    public List<Contact> getAll() {
        return contactList;
    }

    public ArrayList<Contact> getSelected() {
        ArrayList<Contact> selectedList = new ArrayList<>();
        for (Contact contact: contactList) {
            if (contact.isChecked()) {
                selectedList.add(contact);
            }
        }
        return selectedList;
    }

    public Contact getContactPosition(int position) {
        return contactList.get(position);
    }
}