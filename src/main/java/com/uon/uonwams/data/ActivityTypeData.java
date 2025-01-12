/**
 Program: UON WAMS Application
 Filename: ActivityTypeData.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.data;

import com.uon.uonwams.models.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ActivityTypeData {
    private final DATFile DATfile;
    private List<ActivityType> activityTypes;

    // initialize DATFile instance and read data from that file
    public ActivityTypeData(String pathname) throws Exception {
        this.DATfile = new DATFile<ActivityType>(pathname);
        this.activityTypes = DATfile.getData();
    }

    public List<ActivityType> getActivityTypes() {
        return this.activityTypes;
    }

    // read activity types from the CSV file
    public List<ActivityType> readActivityTypesFromCSV(String pathname) throws Exception {
        CSVFile CSVfile = new CSVFile(pathname);
        // parse from List<LinkedHashMap<String, String>> to List<ActivityType>
        return this.parseCSVToActivityTypes(CSVfile.getData());
    }

    // insert a new activity type into DAT file
    public void insertActivityTypesToDAT(List<ActivityType> activityTypes) {
        // insert a new activity type
        DATfile.insertRecords(activityTypes);
        // reload latest data and store them into this.activityTypes
        this.activityTypes = DATfile.getData();
    }

    // convert List<LinkedHashMap<String, String>> to List<ActivityType>
    public List<ActivityType> parseCSVToActivityTypes(List<LinkedHashMap<String, String>> records) throws Exception {
        List<ActivityType> activityTypes = new ArrayList<>();
        for (int i=0; i<records.size(); i++) {
            activityTypes.add(parseCSVToActivityType(i + 1, records.get(i)));
        }
        return activityTypes;
    }

    // convert LinkedHashMap<String, String> to ActivityType
    public ActivityType parseCSVToActivityType(int id, LinkedHashMap<String, String> record) throws Exception {
        LinkedHashMap<String, Double> formula = new LinkedHashMap<>();
        try {
            // convert string to double
            for (String key : record.keySet()) {
                if (!key.equals("Type")) {
                    Double value = Double.parseDouble(record.get(key));
                    formula.put(key, value);
                }
            }
        } catch (Exception e) {
            throw  new Exception("Import Failed: Invalid number in CSV file.\nPlease try again.");
        }
        // create ActivityType instance
        return new ActivityType(
                id,
                record.get("Type"),
                formula
        );
    }
}
