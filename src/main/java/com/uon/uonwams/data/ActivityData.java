package com.uon.uonwams.data;

import com.uon.uonwams.config.ActivityType;
import com.uon.uonwams.config.ContractType;
import com.uon.uonwams.models.CSVFile;
import com.uon.uonwams.models.Activity;
import org.apache.commons.beanutils.ConversionException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ActivityData {
    private final CSVFile file;
    private List<Activity> activities = new ArrayList<>();

//    public static void main(String[] args) throws FileNotFoundException {
//        new ActivityData();
//    }

    public ActivityData() throws Exception {
        file = new CSVFile("files/activity.csv");
        List<LinkedHashMap<String, String>> data = file.getData();
        this.activities = parseActivities(data);
    }

    private int getLatestActivityId() {
        return this.activities.getLast().getActivityId();
    }

    public List<Activity> getActivities() {
        return this.activities;
    }

    public List<String> getAttributes() {
        return this.file.getHeader();
    }

    public Activity insertActivity(String activityName, ActivityType activityType, String description, int responsibleUserId, String responsibleUser, String year, int duration, int weekNo) throws Exception {
        int activityId = this.getLatestActivityId() + 1;
        Activity activity = new Activity(activityId, activityName, activityType, description, responsibleUserId, responsibleUser, year, duration, weekNo);
        file.insertRecord(activity.toHashMap());
        List<LinkedHashMap<String, String>> data = file.getData();
        this.activities = parseActivities(data);
        return activity;
    }

    public Activity updateActivity(int activityId, String activityName, ActivityType type, String description, int responsibleUserId, String responsibleUser, String year, int duration, int weekNo) throws Exception {
        Activity activity = new Activity(activityId, activityName, type, description, responsibleUserId, responsibleUser, year, duration, weekNo);
        file.updateRecord(activity.toHashMap(), "activityId");
        List<LinkedHashMap<String, String>> data = file.getData();
        this.activities = parseActivities(data);
        return activity;
    }

    public int deleteActivity(int activityId) throws Exception {
        file.deleteRecord(activityId);
        List<LinkedHashMap<String, String>> data = file.getData();
        this.activities = parseActivities(data);
        return activityId;
    }

    public List<Activity> parseActivities(List<LinkedHashMap<String, String>> records) throws Exception {
        List<Activity> activities = new ArrayList<>();
        for (LinkedHashMap<String, String> record: records) {
            activities.add(parseActivity(record));
        }
        return activities;
    }


    public Activity parseActivity(LinkedHashMap<String, String> record) throws Exception {
        ActivityType activityType;
        if (record.get("activityType").equals(ActivityType.ATSR.label)) {
            activityType = ActivityType.ATSR;
        } else if (record.get("activityType").equals(ActivityType.TLR.label)) {
            activityType = ActivityType.TLR;
        } else if (record.get("activityType").equals(ActivityType.SA.label)) {
            activityType = ActivityType.SA;
        } else if (record.get("activityType").equals(ActivityType.OTHER.label)) {
            activityType = ActivityType.OTHER;
        } else {
            throw new Exception("Invalid activity type in file");
        }

        return new Activity(
                Integer.parseInt(record.get("activityId")),
                record.get("activityName"),
                activityType,
                record.get("description"),
                Integer.parseInt(record.get("responsibleUserId")),
                record.get("responsibleUser"),
                record.get("year"),
                Integer.parseInt(record.get("duration")),
                Integer.parseInt(record.get("weekNo")),
                Integer.parseInt(record.get("hours")),
                Integer.parseInt(record.get("ATSR")),
                Integer.parseInt(record.get("TS")),
                Integer.parseInt(record.get("TLR")),
                Integer.parseInt(record.get("SA")),
                Integer.parseInt(record.get("other"))
        );
    }
}
