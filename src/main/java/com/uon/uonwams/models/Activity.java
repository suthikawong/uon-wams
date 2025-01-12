/**
 Program: UON WAMS Application
 Filename: Activity.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import com.uon.uonwams.data.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Activity extends DATFileStructure implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int activityId;
    private final String activityName;
    private final String activityType;
    private final String description;
    private final int responsibleUserId;
    private final String responsibleUser;
    private final String year;
    private final int duration;
    private final int noOfInstances;
    private int hours;
    private LinkedHashMap<String, Integer> workloadHours;

    public Activity(int activityId, String activityName, String activityType, String description, int responsibleUserId, String responsibleUser, String year, int duration, int noOfInstances) {
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
            calculateWorkload();
        } catch (Exception e) {
            System.out.println("Cannot create activity: " + e.getMessage());
        }
    }

    // calculate workload allocation
    public void calculateWorkload() throws Exception {
        this.workloadHours = new LinkedHashMap<>();
        this.hours = this.duration * this.noOfInstances;
        // iterate through activity types and store them into this.workloadHours
        for (ActivityType type: Data.activityTypeData.getActivityTypes()) {
            if (type.getName().equals(this.activityType)) {
                LinkedHashMap<String, Double> formula = type.formula;
                for (String key : formula.keySet()) {
                    this.workloadHours.put(key, (int) Math.ceil(this.hours * formula.get(key)));
                }
                return;
            }
        }
        throw new Exception("Invalid activity type");
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

    public String getActivityType() {
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

    public LinkedHashMap<String, Integer> getWorkloadHours() {
        return workloadHours;
    }
}
