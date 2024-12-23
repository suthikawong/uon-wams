package com.uon.uonwams.data;

import com.uon.uonwams.config.ActivityType;
import com.uon.uonwams.models.Activity;
import com.uon.uonwams.models.DATFile;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class ActivityData {
    private final DATFile file;
    private List<Activity> activities;

    public ActivityData() {
        Dotenv dotenv = Dotenv.load();
        String activityFilePath = dotenv.get("ACTIVITY_FILE_PATH");
        file = new DATFile(activityFilePath);
        this.activities = file.getData();
    }

    private int getLatestActivityId() {
        return this.activities.size() > 0 ? this.activities.getLast().getActivityId() : 0;
    }

    public List<Activity> getActivities() {
        return this.activities;
    }

    public List<String> getAttributes() {
        return Arrays.asList("activityId", "activityName", "activityType", "description", "responsibleUserId", "responsibleUser", "year", "duration", "noOfInstances");
    }

    public Activity insertActivity(String activityName, ActivityType activityType, String description, int responsibleUserId, String responsibleUser, String year, int duration, int noOfInstances) {
        int activityId = this.getLatestActivityId() + 1;
        Activity activity = new Activity(activityId, activityName, activityType, description, responsibleUserId, responsibleUser, year, duration, noOfInstances);
        file.insertRecord(activity);
        this.activities = file.getData();
        return activity;
    }

    public void insertActivities(List<LinkedHashMap<String, String>> data) throws Exception {
        List<Activity> records = this.parseActivities(data);
        file.insertRecords(records);
        this.activities = file.getData();
    }

    public Activity updateActivity(int activityId, String activityName, ActivityType type, String description, int responsibleUserId, String responsibleUser, String year, int duration, int noOfInstances) {
        Activity activity = new Activity(activityId, activityName, type, description, responsibleUserId, responsibleUser, year, duration, noOfInstances);
        file.updateRecord(activity);
        this.activities = file.getData();
        return activity;
    }

    public int deleteActivity(int activityId) {
        file.deleteRecord(activityId);
        this.activities = file.getData();
        return activityId;
    }

    public List<Activity> parseActivities(List<LinkedHashMap<String, String>> records) throws Exception {
        List<Activity> activities = new ArrayList<>();
        int activityId = this.getLatestActivityId() + 1;

        for (int i=0; i<records.size(); i++) {
            LinkedHashMap<String, String> record = records.get(i);
            record.put("activityId", Integer.toString(activityId + i));
            activities.add(parseActivity(record));
        }
        return activities;
    }

    public static Activity parseActivity(LinkedHashMap<String, String> record) throws Exception {
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
                Integer.parseInt(record.get("noOfInstances"))
//                Integer.parseInt(record.get("hours")),
//                Integer.parseInt(record.get("ATSR")),
//                Integer.parseInt(record.get("TS")),
//                Integer.parseInt(record.get("TLR")),
//                Integer.parseInt(record.get("SA")),
//                Integer.parseInt(record.get("other"))
        );
    }
}
