/**
 Program: UON WAMS Application
 Filename: ActivityTest.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import com.uon.uonwams.data.Data;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {
    private static String pathname = "files/test-activity-class.dat";
    private static File file;


    @BeforeAll
    public static void setup() throws Exception {
        file = new File(pathname);
        file.createNewFile();

        new Data(null, null, pathname);

        List<ActivityType> list = new ArrayList<>();
        list.add(new ActivityType(1, "ATSR", new LinkedHashMap<>() {{
                put("ATSR", 1.0);
                put("TS", 1.2);
                put("TLR", 0.0);
                put("SA", 0.0);
                put("Other", 0.0);
            }}
        ));
        list.add(new ActivityType(2, "TLR", new LinkedHashMap<>() {{
                put("ATSR", 0.0);
                put("TS", 0.0);
                put("TLR", 1.0);
                put("SA", 0.0);
                put("Other", 0.0);
            }}
        ));
        list.add(new ActivityType(3, "SA", new LinkedHashMap<>() {{
                put("ATSR", 0.0);
                put("TS", 0.0);
                put("TLR", 0.0);
                put("SA", 1.0);
                put("Other", 0.0);
            }}
        ));
        list.add(new ActivityType(4, "Other", new LinkedHashMap<>() {{
                put("ATSR", 0.0);
                put("TS", 0.0);
                put("TLR", 0.0);
                put("SA", 0.0);
                put("Other", 1.0);
            }}
        ));

        Data.configurationData.insertActivityTypes(list);
    }

    @AfterAll
    public static void teardown() throws IOException {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testCreateActivity() throws Exception {
        int activityId = 99;
        String activityName = "test activity";
        String activityType = "ATSR";
        String description = "test";
        int responsibleUserId = 99;
        String responsibleUser = "test";
        String year = "All Year";
        int duration = 2;
        int noOfInstances = 80;

        Activity activity = new Activity(activityId, activityName, activityType, description, responsibleUserId, responsibleUser, year, duration, noOfInstances);

        assertEquals(activityId, activity.getActivityId());
        assertEquals(activityName, activity.getActivityName());
        assertEquals(activityType, activity.getActivityType());
        assertEquals(description, activity.getDescription());
        assertEquals(responsibleUserId, activity.getResponsibleUserId());
        assertEquals(responsibleUser, activity.getResponsibleUser());
        assertEquals(year, activity.getYear());
        assertEquals(duration, activity.getDuration());
        assertEquals(noOfInstances, activity.getNoOfInstances());
    }

    @Test
    void testCalculateActivityATSRType() throws Exception {
        int duration = 2;
        int noOfInstances = 80;
        int totalHours = duration * noOfInstances;
        Activity activity = new Activity(99, "test activity", "ATSR", "test", 99, null, "All Year", 2, 80);
        assertEquals(totalHours, activity.getHours());
        assertEquals(totalHours, activity.getWorkloadHours().get("ATSR"));
        assertEquals((int) Math.ceil(totalHours * 1.2), activity.getWorkloadHours().get("TS"));
        assertEquals(0, activity.getWorkloadHours().get("TLR"));
        assertEquals(0, activity.getWorkloadHours().get("SA"));
        assertEquals(0, activity.getWorkloadHours().get("Other"));
    }

    @Test
    void testCalculateActivityTLRType() throws Exception {
        int duration = 2;
        int noOfInstances = 80;
        int totalHours = duration * noOfInstances;
        Activity activity = new Activity(99, "test activity", "TLR", "test", 99, null, "All Year", 2, 80);
        assertEquals(totalHours, activity.getHours());
        assertEquals(0, activity.getWorkloadHours().get("ATSR"));
        assertEquals(0, activity.getWorkloadHours().get("TS"));
        assertEquals(totalHours, activity.getWorkloadHours().get("TLR"));
        assertEquals(0, activity.getWorkloadHours().get("SA"));
        assertEquals(0, activity.getWorkloadHours().get("Other"));
    }

    @Test
    void testCalculateActivitySAType() throws Exception {
        int duration = 2;
        int noOfInstances = 80;
        int totalHours = duration * noOfInstances;
        Activity activity = new Activity(99, "test activity", "SA", "test", 99, null, "All Year", 2, 80);
        assertEquals(totalHours, activity.getHours());
        assertEquals(0, activity.getWorkloadHours().get("ATSR"));
        assertEquals(0, activity.getWorkloadHours().get("TS"));
        assertEquals(0, activity.getWorkloadHours().get("TLR"));
        assertEquals(totalHours, activity.getWorkloadHours().get("SA"));
        assertEquals(0, activity.getWorkloadHours().get("Other"));
    }

    @Test
    void testCalculateActivityOtherType() throws Exception {
        int duration = 2;
        int noOfInstances = 80;
        int totalHours = duration * noOfInstances;
        Activity activity = new Activity(99, "test activity", "Other", "test", 99, null, "All Year", 2, 80);
        assertEquals(totalHours, activity.getHours());
        assertEquals(0, activity.getWorkloadHours().get("ATSR"));
        assertEquals(0, activity.getWorkloadHours().get("TS"));
        assertEquals(0, activity.getWorkloadHours().get("TLR"));
        assertEquals(0, activity.getWorkloadHours().get("SA"));
        assertEquals(totalHours, activity.getWorkloadHours().get("Other"));
    }
}