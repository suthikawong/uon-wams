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

import java.util.List;

public class UserWorkloadAllocation extends User {
    private int totalHours = 0;
    private int fteHours = 0;
    private int totalAtsrTs = 0;
    private double percentageOfAtsrAllocated = 0;
    private double parcentageOfTotalHoursAllocated = 0;
    private int fteAtsrHours = 0;
    private final int MaxFteHours = 1570;
    private final int MaxFteAtsrHours = 550;

    // calculate workload allocation of that user when initializing a new instance
    public UserWorkloadAllocation(User user) {
        super(user.getUserId(), user.getName(), user.getPassword(), user.getEmail(), user.getFteRatio(), user.getSubjectArea(), user.getLineManagerUserId());
        calculateWorkloadAllocation();
    }

    // calculate workload allocation
    private void calculateWorkloadAllocation() {
        // get all activities in the system
        List<Activity> activities = Data.activityData.getActivities();
        int scale = (int) Math.pow(10, 1); // multiplier for converting double to have one decimal place
        int sumATSR = 0;
        int sumTS = 0;
        int sumTLR = 0;
        int sumSA = 0;
        int sumOther = 0;
        for (Activity activity: activities) {
            if (this.userId == activity.getResponsibleUserId()) {
                sumATSR += activity.getATSR();
                sumTS += activity.getTS();
                sumTLR += activity.getTLR();
                sumSA += activity.getSA();
                sumOther += activity.getOther();
            }
        }
        // using Math.ceil to round up number
        // using scale to get number that have one decimal place
        this.totalHours = sumATSR + sumTS + sumTLR + sumSA + sumOther;
        this.fteHours = (int) Math.ceil(MaxFteHours * this.fteRatio);
        this.totalAtsrTs = sumATSR + sumTS;
        this.percentageOfAtsrAllocated = Math.ceil(((double) sumATSR / (MaxFteHours * this.fteRatio) * 100) * scale) / scale;
        this.parcentageOfTotalHoursAllocated = Math.ceil(((double) this.totalHours / (MaxFteHours * this.fteRatio) * 100) * scale) / scale;
        this.fteAtsrHours = (int) Math.ceil(MaxFteAtsrHours * this.fteRatio);
    }

    public int getTotalHours() {
        return totalHours;
    }

    public int getFteHours() {
        return fteHours;
    }

    public int getTotalAtsrTs() {
        return totalAtsrTs;
    }

    public double getPercentageOfAtsrAllocated() {
        return percentageOfAtsrAllocated;
    }

    public double getParcentageOfTotalHoursAllocated() {
        return parcentageOfTotalHoursAllocated;
    }

    public int getFteAtsrHours() {
        return fteAtsrHours;
    }
}
