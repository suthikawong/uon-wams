/**
 Program: UON WAMS Application
 Filename: UserWorkloadAllocation.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import com.uon.uonwams.data.Data;
import javafx.scene.control.TableColumn;

import java.util.List;

public class UserWorkloadAllocation extends User {
    private int totalHours = 0;
    private int fteHours = 0;
    private double parcentageOfTotalHoursAllocated = 0;
    private final int MaxFteHours = 1570;

    // calculate workload allocation of that user when initializing a new instance
    public UserWorkloadAllocation(User user) {
        super(user.getUserId(), user.getName(), user.getPassword(), user.getEmail(), user.getFteRatio(), user.getSubjectArea(), user.getLineManagerUserId());
        calculateWorkloadAllocation();
    }

    // calculate workload allocation
    public void calculateWorkloadAllocation() {
        // get all activities in the system
        List<Activity> activities = Data.activityData.getActivities();
        int scale = (int) Math.pow(10, 1); // multiplier for converting double to have one decimal place
        int sum = 0;
        for (Activity activity: activities) {
            if (this.userId == activity.getResponsibleUserId()) {
                for(String key : activity.getWorkloadHours().keySet()) {
                    sum += activity.getWorkloadHours().get(key);
                }
            }
        }
        // using Math.ceil to round up number
        // using scale to get number that have one decimal place
        this.totalHours = sum;
        this.fteHours = (int) Math.ceil(MaxFteHours * this.fteRatio);
        this.parcentageOfTotalHoursAllocated = Math.ceil(((double) this.totalHours / (MaxFteHours * this.fteRatio) * 100) * scale) / scale;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public int getFteHours() {
        return fteHours;
    }

    public double getParcentageOfTotalHoursAllocated() {
        return parcentageOfTotalHoursAllocated;
    }
}
