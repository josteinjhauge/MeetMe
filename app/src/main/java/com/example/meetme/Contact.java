package com.example.meetme;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "contact_table")
public class Contact implements Serializable, Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contactId")
    private int contactId;

    @NonNull
    @ColumnInfo(name = "contactName")
    private String contactName;

    @NonNull
    @ColumnInfo(name = "contactNumber")
    private String contactNumber;

    @Ignore
    public Contact(int contactId, @NonNull String contactName,
                   @NonNull String contactNumber) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public Contact(@NonNull String contactName,
                   @NonNull String contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactId = 0;
    }

    protected Contact(Parcel in) {
        contactName = in.readString();
        contactNumber = in.readString();
        contactId = in.readInt();
    }

    public int getContactId() {
        return contactId;
    }

    @NonNull
    public String getContactName() {
        return contactName;
    }

    @NonNull
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setContactName(@NonNull String contactName) {
        this.contactName = contactName;
    }

    public void setContactNumber(@NonNull String contactNumber) {
        this.contactNumber = contactNumber;
    }

    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(contactName);
        parcel.writeString(contactNumber);
    }
}