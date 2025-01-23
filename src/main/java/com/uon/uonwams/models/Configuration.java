/**
 Program: UON WAMS Application
 Filename: Configuration.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;
import java.io.Serializable;
import java.util.List;

public class Configuration extends DATFileStructure implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int id;
    protected int totalFullTimeHours;
    protected List<ActivityType> activityTypes;

    public Configuration(int totalFullTimeHours, List<ActivityType> activityTypes) {
        this.totalFullTimeHours = totalFullTimeHours;
        this.activityTypes = activityTypes;
    }

    public int getId() {
        return id;
    }

    public int getTotalFullTimeHours() {
        return totalFullTimeHours;
    }

    public List<ActivityType> getActivityTypes() {
        return activityTypes;
    }

    public void setTotalFullTimeHours(int totalFullTimeHours) {
        this.totalFullTimeHours = totalFullTimeHours;
    }

    public void setActivityTypes(List<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
    }
}
