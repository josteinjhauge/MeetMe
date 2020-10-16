package com.example.meetme;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class MeetMeViewModel extends AndroidViewModel {

    private MeetMeRepository rep;

    private LiveData<List<Meeting>> allMeetings;
    private LiveData<List<Contact>> allContacts;
    private LiveData<List<MeetingParti>> allMeetingParties;

    public MeetMeViewModel (@NonNull Application application) {
        super(application);
        rep = new MeetMeRepository(application);

        allMeetings = rep.getAllMeetings();
        allContacts = rep.getAllContacts();
        allMeetingParties = rep.getAllMeetingParties();
    }

    Meeting getMeeting(int id) {
        return rep.getMeeting(id);
    }
    Contact getContact(int id) {
        return rep.getContact(id);
    }
    MeetingParti getMeetingParties(int id) {
        return rep.getMeetingParties(id);
    }

    LiveData<List<Meeting>> getAllMeetings() {
        return allMeetings;
    }
    LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }
    LiveData<List<MeetingParti>> getAllMeetingParties() {
        return allMeetingParties;
    }

    public void insert(Meeting meeting) {
            rep.insert(meeting);
    }
    public void insert(Contact contact) {
            rep.insert(contact);
    }
    public void insert(MeetingParti meetingParti) {
            rep.insert(meetingParti);
    }

    public void updateMeeting(Meeting newMeeting) {
        rep.updateMeeting(newMeeting);
    }
    public void updateContact(Contact newContact) {
        rep.updateContact(newContact);
    }
    public void updateMeetingParti(MeetingParti meetingParti) {
        rep.updateMeetingParti(meetingParti);
    }

    public void deleteMeetingPartiByMeetingId (int id) {
        rep.deleteMeetingPartiByMeetingId(id);
    }
    public void deleteMeetingPartiByContactId (int id) {
        rep.deleteMeetingPartiByContactId(id);
    }


    public void delete(Meeting meeting) {
            rep.delete(meeting);
    }
    public void delete(Contact contact) {
            rep.delete(contact);
    }
    public void delete(MeetingParti meetingParti) {
            rep.delete(meetingParti);
    }
}