package com.example.meetme;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "meeting_table")
public class Meeting {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "meetingId")
    private int meetingId;

    @NonNull
    @ColumnInfo(name = "meetingName")
    private String meetingName;

    @NonNull
    @ColumnInfo(name = "meetingLocation")
    private String meetingLocation;

    @NonNull
    @ColumnInfo(name = "meetingType")
    private String meetingType;

    @NonNull
    @ColumnInfo(name = "startTime")
    private String startTime;

    @NonNull
    @ColumnInfo(name = "endTime")
    private String endTime;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    public Meeting(@NonNull String meetingName,
                   @NonNull String meetingLocation, @NonNull String meetingType,
                   @NonNull String startTime, @NonNull String endTime,
                   @NonNull String date) {
        this.meetingId = 0;
        this.meetingName = meetingName;
        this.meetingLocation = meetingLocation;
        this.meetingType = meetingType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    @Ignore
    public Meeting(int meetingId, String name,
                   String location, String type,
                   String startTime, String endTime,
                   String date) {
        this.meetingId = meetingId;
        this.meetingName = name;
        this.meetingLocation = location;
        this.meetingType = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public int getMeetingId() {
        return meetingId;
    }

    @NonNull
    public String getMeetingName() {
        return meetingName;
    }

    @NonNull
    public String getMeetingType() {
        return meetingType;
    }

    @NonNull
    public String getMeetingLocation() {
        return meetingLocation;
    }

    @NonNull
    public String getStartTime() {
        return startTime;
    }

    @NonNull
    public String getEndTime() {
        return endTime;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }
}