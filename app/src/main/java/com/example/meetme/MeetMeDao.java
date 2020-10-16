package com.example.meetme;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MeetMeDao {


    @Query("SELECT * from meeting_table WHERE meetingId = :id")
    Meeting getMeeting(int id);

    @Query("SELECT * from contact_table WHERE contactId = :id")
    Contact getContact(int id);

    @Query("SELECT * from meetingParti_table WHERE meetingId = :id")
    MeetingParti getMeetingParties(int id);



    @Query("SELECT * from meeting_table ORDER BY meetingId ASC")
    LiveData<List<Meeting>> getAllMeetings();

    @Query("SELECT * from contact_table ORDER BY contactName ASC")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * from meetingParti_table ORDER BY meetingId ASC")
    LiveData<List<MeetingParti>> getAllMeetingParties();

    @Query("SELECT * FROM contact_table")
    Cursor getAllContactsForCP();

    @Query("SELECT * FROM contact_table WHERE contactId = :id")
    Cursor getContactForCP(long id);




    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Meeting meeting);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact contact);

    // SPØRS OM DENNE MÅ VÆRE MED ?????
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MeetingParti meetingParti);



    @Update
    void updateMeeting(Meeting meeting);

    @Update
    void updateContact(Contact contact);

    @Update
    void updateMeetingParti(MeetingParti meetingParti);


    @Query("DELETE from meetingParti_table WHERE meetingId = :id")
    void deleteMeetingPartiByMeetingId (int id);

    @Query("DELETE from meetingParti_table WHERE contactId = :id")
    void deleteMeetingPartiByContactId (int id);

    @Delete
    void delete(Meeting meeting);

    @Delete
    void delete(Contact contact);

    @Delete
    void delete(MeetingParti meetingParti);
}