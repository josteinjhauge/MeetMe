package com.example.meetme;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Meeting.class,
        Contact.class, MeetingParti.class}, version = 1, exportSchema = false)
public abstract class MeetMeDatabase extends RoomDatabase {

    public abstract MeetMeDao meetMeDao();

    private static volatile MeetMeDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MeetMeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MeetMeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MeetMeDatabase.class, "meetme_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}