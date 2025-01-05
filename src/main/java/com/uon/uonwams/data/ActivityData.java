package com.uon.uonwams.data;

import com.uon.uonwams.config.ActivityType;
import com.uon.uonwams.models.Activity;
import com.uon.uonwams.models.DATFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class ActivityData {
    private final DATFile file;
    private List<Activity> activities;

    // initialize DATFile instance and read data from that file
    public ActivityData(String pathname) {
        file = new DATFile(pathname);
        this.activities = file.getData();
    }

    // get the latest activityId from DAT file
    private int getLatestActivityId() {
        return this.activities.size() > 0 ? this.activities.getLast().getActivityId() : 0;
    }

    // get activities from DAT file
    public List<Activity> getActivities() {
        return this.activities;
    }

    public List<String> getAttributes() {
        return Arrays.asList("activityId", "activityName", "activityType", "description", "responsibleUserId", "responsibleUser", "year", "duration", "noOfInstances");
    }

    // insert a new activity into DAT file
    public void insertActivity(String activityName, ActivityType activityType, String description, int responsibleUserId, String responsibleUser, String year, int duration, int noOfInstances) {
        // get the latest activityId in DAT file and increase it by 1 to get a new uniquely activityId
        int activityId = this.getLatestActivityId() + 1;
        // initialize activity instance
        Activity activity = new Activity(activityId, activityName, activityType, description, responsibleUserId, responsibleUser, year, duration, noOfInstances);
        // insert a new activity
        file.insertRecord(activity);
        // reload latest data and store them into this.activities
        this.activities = file.getData();
    }

    // insert multiple activities into DAT file
    public void insertActivities(List<LinkedHashMap<String, String>> data) throws Exception {
        // convert data from the list of LinkedHashMap type into the list of Activity type
        List<Activity> records = this.parseActivities(data);
        // insert activities
        file.insertRecords(records);
        // reload latest data and store them into this.activities
        this.activities = file.getData();
    }

    // update activity in the DAT file
    public void updateActivity(int activityId, String activityName, ActivityType type, String description, int responsibleUserId, String responsibleUser, String year, int duration, int noOfInstances) {
        Activity activity = new Activity(activityId, activityName, type, description, responsibleUserId, responsibleUser, year, duration, noOfInstances);
        // update activities
        file.updateRecord(activity);
        // reload latest data and store them into this.activities
        this.activities = file.getData();
    }

    // delete activity from the DAT file
    public void deleteActivity(int activityId) {
        // delete activities
        file.deleteRecord(activityId);
        // reload latest data and store them into this.activities
        this.activities = file.getData();
    }

    // convert data from the list of LinkedHashMap type into the list of Activity type
    public List<Activity> parseActivities(List<LinkedHashMap<String, String>> records) throws Exception {
        List<Activity> activities = new ArrayList<>();
        // get the latest activityId in DAT file and increase it by 1 to get a new uniquely activityId
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
        );
    }
}
