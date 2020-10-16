package com.example.meetme;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity (tableName = "meetingParti_table",
        primaryKeys = {"meetingId", "contactId"})
public class MeetingParti {

    @ForeignKey(entity = Meeting.class,
            parentColumns = "meetingId", childColumns = "meetingId")
    @NonNull
    @ColumnInfo(name = "meetingId")
    private int meetingId;


    @ForeignKey(entity = Contact.class,
            parentColumns = "contactId", childColumns = "contactId")
    @NonNull
    @ColumnInfo(name = "contactId")
    private int contactId;

    public MeetingParti(@NonNull int meetingId,
                        @NonNull int contactId) {
        this.meetingId = meetingId;
        this.contactId = contactId;
    }

    @NonNull
    public int getMeetingId() {
        return meetingId;
    }

    @NonNull
    public int getContactId() {
        return contactId;
    }
}