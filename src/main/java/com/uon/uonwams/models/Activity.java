/**
 Program: UON WAMS Application
 Filename: Activity.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import com.uon.uonwams.config.ActivityType;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Activity extends DATFileStructure implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int activityId;
    private final String activityName;
    private final ActivityType activityType;
    private final String description;
    private final int responsibleUserId;
    private final String responsibleUser;
    private final String year;
    private final int duration;
    private final int noOfInstances;
    private int hours;
    private int ATSR;
    private int TS;
    private int TLR;
    private int SA;
    private int other;

    public Activity(int activityId, String activityName, ActivityType activityType, String description, int responsibleUserId, String responsibleUser, String year, int duration, int noOfInstances) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityType = activityType;
        this.description = description;
        this.responsibleUserId = responsibleUserId;
        this.responsibleUser = responsibleUser;
        this.year = year;
        this.duration = duration;
        this.noOfInstances = noOfInstances;

        try {
            // calculate ATSR, TS, TLR, SA and Other hours for activity
            calculateWorkload(activityType, duration, noOfInstances);
        } catch (Exception e) {
            System.out.println("Cannot create activity: " + e.getMessage());
        }
    }

    private void calculateWorkload(ActivityType activityType, int duration, int noOfInstances) throws Exception {
        this.hours = duration * noOfInstances;
        switch (activityType) {
            case ActivityType.ATSR:
                this.ATSR = this.hours;
                this.TS = (int) Math.ceil(this.hours * 1.2); // round up if result is decimal number
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

    public int getId() {
        return activityId;
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

    public int getNoOfInstances() {
        return noOfInstances;
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
}
