package com.example.meetme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {


    public interface OnContactListener {
        void onContactClick(Contact contact);
    }

    public void setOnContactListener(OnContactListener listener) {
        this.listener = listener;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private final TextView itemNumber;
        private OnContactListener listener;

        private ContactViewHolder(View itemView, OnContactListener listener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemNumber = itemView.findViewById(R.id.item_number);
            this.listener = listener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onContactClick(contactList.get(position));
                    }
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<Contact> contactList;
    private OnContactListener listener;

    ContactAdapter(Context context, OnContactListener listener) {
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.contact_recycler_item, parent, false);
        return new ContactViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        if (contactList != null) {
            Contact current = contactList.get(position);
            holder.itemName.setText(current.getContactName());
            holder.itemNumber.setText(current.getContactNumber());
        } else {
            holder.itemName.setText(R.string.noDataWarning);
            holder.itemNumber.setText(R.string.noDataWarning);
        }
    }

    void setContact(List<Contact> contacts) {
        contactList = contacts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (contactList != null) {
            return contactList.size();
        } else {
            return 0;
        }
    }

    public Contact getContactPosition(int position) {
        return contactList.get(position);
    }
}