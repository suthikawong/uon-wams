package com.uon.uonwams.data;

import com.uon.uonwams.models.CSVFile;
import com.uon.uonwams.models.Activity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityData {
    private final List<Activity> activities = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        new ActivityData();
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public ActivityData() throws FileNotFoundException {
        CSVFile file = new CSVFile("files/activity.csv");
        List<HashMap<String, String>> data = file.getData();
        for (HashMap<String, String> record: data) {
            this.activities.add(parseActivity(record));
        }
    }

    public Activity parseActivity(HashMap<String, String> record) {
        return new Activity(
                Integer.parseInt(record.get("activityId")),
                record.get("activityName"),
                record.get("type"),
                record.get("description"),
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
