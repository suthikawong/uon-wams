/**
 Program: UON WAMS Application
 Filename: User.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import com.uon.uonwams.data.Data;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
