package com.uon.uonwams.models;

import com.uon.uonwams.config.ActivityType;

import java.util.LinkedHashMap;

public class Activity {
    private final int activityId;
    private final String activityName;
    private final ActivityType activityType;
    private final String description;
    private final int responsibleUserId;
    private final String responsibleUser;
    private final String year;
    private final int duration;
    private final int weekNo;
    private int hours;
    private int ATSR;
    private int TS;
    private int TLR;
    private int SA;
    private int other;

    public Activity(int activityId, String activityName, ActivityType activityType, String description, int responsibleUserId, String responsibleUser, String year, int duration, int weekNo) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityType = activityType;
        this.description = description;
        this.responsibleUserId = responsibleUserId;
        this.responsibleUser = responsibleUser;
        this.year = year;
        this.duration = duration;
        this.weekNo = weekNo;

        try {
            calculateWorkload(activityType, duration, weekNo);
        } catch (Exception e) {
            System.out.println("Cannot create activity: " + e.getMessage());
        }
    }

    private void calculateWorkload(ActivityType activityType, int duration, int weekNo) throws Exception {
        this.hours = duration * weekNo;
        switch (activityType) {
            case ActivityType.ATSR:
                this.ATSR = this.hours;
                this.TS = (int) Math.ceil(this.hours * 1.2);
                this.TLR = 0;
                this.SA = 0;
                this.other = 0;
                break;
            case ActivityType.TLR:
                this.ATSR = 0;
                this.TS = 0;
                this.TLR = this.hours;
                this.SA = 0;
                this.other = 0;
                break;
            case ActivityType.SA:
                this.ATSR = 0;
                this.TS = 0;
                this.TLR = 0;
                this.SA = this.hours;
                this.other = 0;
                break;
            case ActivityType.OTHER:
                this.ATSR = 0;
                this.TS = 0;
                this.TLR = 0;
                this.SA = 0;
                this.other = this.hours;
                break;
            default:
                throw new Exception("Invalid activity type");
        }
    }


    public Activity(int activityId, String activityName, ActivityType activityType, String description, int responsibleUserId, String responsibleUser, String year, int duration, int weekNo, int hours, int ATSR, int TS, int TLR, int SA, int other) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityType = activityType;
        this.description = description;
        this.responsibleUserId = responsibleUserId;
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

    public int getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public String getDescription() {
        return description;
    }

    public int getResponsibleUserId() {
        return responsibleUserId;
    }

    public String getResponsibleUser() {
        return responsibleUser;
    }

    public String getYear() {
        return year;
    }

    public int getDuration() {
        return duration;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public int getHours() {
        return hours;
    }

    public int getATSR() {
        return ATSR;
    }

    public int getTS() {
        return TS;
    }

    public int getTLR() {
        return TLR;
    }

    public int getSA() {
        return SA;
    }

    public int getOther() {
        return other;
    }

    public LinkedHashMap<String, String> toHashMap() {
        LinkedHashMap<String, String> mapActivity = new LinkedHashMap<String, String>();
        mapActivity.put("activityId", Integer.toString(this.activityId));
        mapActivity.put("activityType", this.activityType.label);
        mapActivity.put("activityName", this.activityName);
        mapActivity.put("description", this.description);
        mapActivity.put("responsibleUserId", Integer.toString(this.responsibleUserId));
        mapActivity.put("responsibleUser", this.responsibleUser);
        mapActivity.put("year", this.year);
        mapActivity.put("duration", Integer.toString(this.duration));
        mapActivity.put("weekNo", Integer.toString(this.weekNo));
        mapActivity.put("hours", Integer.toString(this.hours));
        mapActivity.put("ATSR", Integer.toString(this.ATSR));
        mapActivity.put("TS", Integer.toString(this.TS));
        mapActivity.put("TLR", Integer.toString(this.TLR));
        mapActivity.put("SA", Integer.toString(this.SA));
        mapActivity.put("other", Integer.toString(this.other));
        return mapActivity;
    }

    @Override
    public String toString() {
        return "activityName='" + activityName + '\'' +
                ", activityType='" + activityType + '\'' +
                ", description='" + description + '\'' +
                ", responsibleUserId=" + responsibleUserId +
                ", responsibleUser='" + responsibleUser + '\'' +
                ", year='" + year + '\'' +
                ", duration=" + duration +
                ", weekNo=" + weekNo +
                ", hours=" + hours +
                ", ATSR=" + ATSR +
                ", TS=" + TS +
                ", TLR=" + TLR +
                ", SA=" + SA +
                ", other=" + other;
    }
}
