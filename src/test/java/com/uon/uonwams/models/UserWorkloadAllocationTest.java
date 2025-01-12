/**
 Program: UON WAMS Application
 Filename: UserWorkloadAllocationTest.java
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

import static com.uon.uonwams.models.User.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

class UserWorkloadAllocationTest {
    private static String activityPathname = "files/test-userworkloadallocation-activity-class.dat";
    private static String activityTypePathname = "files/test-userworkloadallocation-activitytype-class.dat";
    private static File activityFile;
    private static File activityTypeFile;
    private static User user;

    @BeforeAll
    public static void setup() throws Exception {
        activityFile = new File(activityPathname);
        activityFile.createNewFile();
        activityTypeFile = new File(activityTypePathname);
        activityTypeFile.createNewFile();

        new Data(null, activityPathname, activityTypePathname);

        List<ActivityType> activityTypesList = new ArrayList<>();
        activityTypesList.add(new ActivityType(1, "ATSR", new LinkedHashMap<>() {{
            put("ATSR", 1.0);
            put("TS", 1.2);
            put("TLR", 0.0);
            put("SA", 0.0);
            put("Other", 0.0);
        }}
        ));
        activityTypesList.add(new ActivityType(2, "TLR", new LinkedHashMap<>() {{
            put("ATSR", 0.0);
            put("TS", 0.0);
            put("TLR", 1.0);
            put("SA", 0.0);
            put("Other", 0.0);
        }}
        ));
        activityTypesList.add(new ActivityType(3, "SA", new LinkedHashMap<>() {{
            put("ATSR", 0.0);
            put("TS", 0.0);
            put("TLR", 0.0);
            put("SA", 1.0);
            put("Other", 0.0);
        }}
        ));
        activityTypesList.add(new ActivityType(4, "Other", new LinkedHashMap<>() {{
            put("ATSR", 0.0);
            put("TS", 0.0);
            put("TLR", 0.0);
            put("SA", 0.0);
            put("Other", 1.0);
        }}
        ));

        Data.activityTypeData.insertActivityTypesToDAT(activityTypesList);

        user = new User(1, "testname", hashPassword("password"), "email", 1, "sucjectarea", null);

        Data.activityData.insertActivity("actname1", "ATSR", "test1", user.userId, user.name, "All Year", 2, 100);
        Data.activityData.insertActivity("actname2", "SA", "test2", user.userId, user.name, "All Year", 1, 30);
    }

    @AfterAll
    public static void teardown() throws IOException {
        if (activityTypeFile.exists()) {
            activityTypeFile.delete();
        }
        if (activityFile.exists()) {
            activityFile.delete();
        }
    }

    @Test
    void testCalculateWorkloadAllocation() {
        List<Activity> activities = Data.activityData.getActivities();
        UserWorkloadAllocation userWorkloadAllocation = new UserWorkloadAllocation(user);
        assertEquals(470, userWorkloadAllocation.getTotalHours());
        assertEquals(1570, userWorkloadAllocation.getFteHours());
        assertEquals(30, userWorkloadAllocation.getParcentageOfTotalHoursAllocated());
    }
}