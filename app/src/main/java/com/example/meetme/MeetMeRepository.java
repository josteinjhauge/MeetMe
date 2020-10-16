package com.example.meetme;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

public class MeetMeRepository {

    private MeetMeDao meetMeDao;
    private LiveData<List<Meeting>> allMeetings;
    private LiveData<List<Contact>> allContacts;
    private LiveData<List<MeetingParti>> allMeetingParties;

    MeetMeRepository(Application application) {
        MeetMeDatabase db = MeetMeDatabase.getDatabase(application);
        meetMeDao = db.meetMeDao();
        allMeetings = meetMeDao.getAllMeetings();
        allContacts = meetMeDao.getAllContacts();
        allMeetingParties = meetMeDao.getAllMeetingParties();
    }

    Meeting getMeeting(int id) {
        return meetMeDao.getMeeting(id);
    }
    Contact getContact(int id) {
        return meetMeDao.getContact(id);
    }
    MeetingParti getMeetingParties(int id) {
        return meetMeDao.getMeetingParties(id);
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

    // Insert funksjoner
    void insert(Meeting meeting) {
        new InsertMeetingAsyncTask(meetMeDao).execute(meeting);
    }
    void insert(Contact contact) {
        new InsertContactAsyncTask(meetMeDao).execute(contact);
    }
    void insert(MeetingParti meetingParti) {
        new InsertMeetingPartiAsyncTask(meetMeDao).execute(meetingParti);
    }

    // Update funksjoner
    void updateMeeting(Meeting meeting) {
        new UpdateMeetingAsyncTask(meetMeDao).execute(meeting);
    }
    void updateContact(Contact contact) {
        new UpdateContactAsyncTask(meetMeDao).execute(contact);
    }
    void updateMeetingParti(MeetingParti meetingParti) {
        new UpdateMeetingPartiAsyncTask(meetMeDao).execute(meetingParti);
    }

    // delete funksjoner
    void deleteMeetingPartiByMeetingId (int id) {
        new DeleteMeetingPartiByMeetingIdAsyncTask(meetMeDao).execute(id);
    }
    void deleteMeetingPartiByContactId (int id) {
        new DeleteMeetingPartiByContactIdAsyncTask(meetMeDao).execute(id);
    }

    void delete(Meeting meeting) {
        new DeleteMeetingAsyncTask(meetMeDao).execute(meeting);
    }
    void delete(Contact contact) {
        new DeleteContactAsyncTask(meetMeDao).execute(contact);
    }
    void delete(MeetingParti meetingParti) {
        new DeleteMeetingPartiAsyncTask(meetMeDao).execute(meetingParti);
    }

    private static class DeleteMeetingPartiByMeetingIdAsyncTask extends AsyncTask<Integer, Void, Void>{
        private MeetMeDao meetMeDao;

        private DeleteMeetingPartiByMeetingIdAsyncTask(MeetMeDao meetMeDao){
            this.meetMeDao = meetMeDao;
        }
        @Override
        protected Void doInBackground(Integer... id) {
            meetMeDao.deleteMeetingPartiByMeetingId(id[0]);
            return null;
        }
    }

    private static class DeleteMeetingPartiByContactIdAsyncTask extends AsyncTask<Integer, Void, Void>{
        private MeetMeDao meetMeDao;

        private DeleteMeetingPartiByContactIdAsyncTask(MeetMeDao meetMeDao){
            this.meetMeDao = meetMeDao;
        }
        @Override
        protected Void doInBackground(Integer... id) {
            meetMeDao.deleteMeetingPartiByContactId(id[0]);
            return null;
        }
    }

    private static class InsertContactAsyncTask extends AsyncTask<Contact, Void, Void>{
        private MeetMeDao meetMeDao;

        private InsertContactAsyncTask(MeetMeDao meetMeDao){
            this.meetMeDao = meetMeDao;
        }
        @Override
        protected Void doInBackground(Contact... contacts) {
            meetMeDao.insert(contacts[0]);
            return null;
        }
    }

    private static class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void>{
        private MeetMeDao meetMeDao;

        private UpdateContactAsyncTask(MeetMeDao meetMeDao){
            this.meetMeDao = meetMeDao;
        }
        @Override
        protected Void doInBackground(Contact... contacts) {
            meetMeDao.updateContact(contacts[0]);
            return null;
        }
    }

    private static class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void>{
        private MeetMeDao meetMeDao;

        private DeleteContactAsyncTask(MeetMeDao meetMeDao){
            this.meetMeDao = meetMeDao;
        }
        @Override
        protected Void doInBackground(Contact... contacts) {
            meetMeDao.delete(contacts[0]);
            return null;
        }
    }

    private static class InsertMeetingAsyncTask extends AsyncTask<Meeting, Void, Void>{
        private MeetMeDao meetMeDao;

        private InsertMeetingAsyncTask(MeetMeDao meetMeDao){
            this.meetMeDao = meetMeDao;
        }
        @Override
        protected Void doInBackground(Meeting... meetings) {
            meetMeDao.insert(meetings[0]);
            return null;
        }
    }

    private static class UpdateMeetingAsyncTask extends AsyncTask<Meeting, Void, Void>{
        private MeetMeDao meetMeDao;

        private UpdateMeetingAsyncTask(MeetMeDao meetMeDao){
            this.meetMeDao = meetMeDao;
        }
        @Override
        protected Void doInBackground(Meeting... meetings) {
            meetMeDao.updateMeeting(meetings[0]);
            return null;
        }
    }

    private static class DeleteMeetingAsyncTask extends AsyncTask<Meeting, Void, Void>{
        private MeetMeDao meetMeDao;

        private DeleteMeetingAsyncTask(MeetMeDao meetMeDao){
            this.meetMeDao = meetMeDao;
        }
        @Override
        protected Void doInBackground(Meeting... meetings) {
            meetMeDao.delete(meetings[0]);
            return null;
        }
    }

    private static class InsertMeetingPartiAsyncTask extends AsyncTask<MeetingParti, Void, Void>{
        private MeetMeDao meetMeDao;

        private InsertMeetingPartiAsyncTask(MeetMeDao meetMeDao){
            this.meetMeDao = meetMeDao;
        }
        @Override
        protected Void doInBackground(MeetingParti... meetingPartis) {
            meetMeDao.insert(meetingPartis[0]);
            return null;
        }
    }

    private static class UpdateMeetingPartiAsyncTask extends AsyncTask<MeetingParti, Void, Void>{
        private MeetMeDao meetMeDao;

        private UpdateMeetingPartiAsyncTask(MeetMeDao meetMeDao){
            this.meetMeDao = meetMeDao;
        }
        @Override
        protected Void doInBackground(MeetingParti... meetingPartis) {
            meetMeDao.updateMeetingParti(meetingPartis[0]);
            return null;
        }
    }

    private static class DeleteMeetingPartiAsyncTask extends AsyncTask<MeetingParti, Void, Void>{
        private MeetMeDao meetMeDao;

        private DeleteMeetingPartiAsyncTask(MeetMeDao meetMeDao){
            this.meetMeDao = meetMeDao;
        }
        @Override
        protected Void doInBackground(MeetingParti... meetingPartis) {
            meetMeDao.delete(meetingPartis[0]);
            return null;
        }
    }
}