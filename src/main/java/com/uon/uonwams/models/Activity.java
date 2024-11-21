package com.uon.uonwams.models;

public class Activity {
    private final int activityId;
    private final String activityName;
    private final String type;
    private final String description;
    private final String responsibleUser;
    private final String year;
    private final int duration;
    private final int weekNo;
    private final int hours;
    private final int ATSR;
    private final int TS;
    private final int TLR;
    private final int SA;
    private final int other;


    public Activity(int activityId, String activityName, String type, String description, String responsibleUser, String year, int duration, int weekNo, int hours, int ATSR, int TS, int TLR, int SA, int other) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.type = type;
        this.description = description;
        this.responsibleUser = responsibleUser;
        this.year = year;
        this.duration = duration;
        this.weekNo = weekNo;
        this.hours = hours;
        this.ATSR = ATSR;
        this.TS = TS;
        this.TLR = TLR;
        this.SA = SA;
        this.other = other;
    }
}
