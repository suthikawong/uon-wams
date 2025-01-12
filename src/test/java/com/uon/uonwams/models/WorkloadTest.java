/**
 Program: UON WAMS Application
 Filename: WorkloadTest.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import com.uon.uonwams.data.Data;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static com.uon.uonwams.models.User.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WorkloadTest {
    private static String userPathname = "files/test-workload-user-class.dat";
    private static String activityPathname = "files/test-workload-activity-class.dat";
    private static String activityTypePathname = "files/test-workload-activitytype-class.dat";
    private static File userFile;
    private static File activityFile;
    private static File activityTypeFile;
    private static DATFile userDatFile;
    private static User lineManager;
    private static User sub;
    private static Activity subActivity;
    private static Workload workload;

    @BeforeAll
    public static void setup() throws Exception {
        userFile = new File(userPathname);
        userFile.createNewFile();
        userDatFile = new DATFile<User>(userPathname);
        activityFile = new File(activityPathname);
        activityFile.createNewFile();
        activityTypeFile = new File(activityTypePathname);
        activityTypeFile.createNewFile();

        lineManager = new User(1, "testname", hashPassword("password"), "email", 1, "sucjectarea", null);
        sub = new User(2, "testname", hashPassword("password"), "email", 1, "sucjectarea", 1);
        userDatFile.appendRecords(List.of(lineManager, sub));

        new Data(userPathname, activityPathname, activityTypePathname);

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

        Data.configurationData.insertActivityTypes(activityTypesList);

        subActivity = new Activity(1, "activityname", "ATSR", "test", sub.userId, sub.name, "All Year", 2, 80);
        Data.activityData.insertActivity(subActivity.getActivityName(), subActivity.getActivityType(), subActivity.getDescription(), subActivity.getResponsibleUserId(), subActivity.getResponsibleUser(), subActivity.getYear(), subActivity.getDuration(), subActivity.getNoOfInstances());

        workload = new Workload(lineManager);
    }

    @AfterAll
    public static void teardown() {
        if (userFile.exists()) {
            userFile.delete();
        }
        if (activityFile.exists()) {
            activityFile.delete();
        }
        if (activityTypeFile.exists()) {
            activityTypeFile.delete();
        }
    }

    @Test
    @Order(1)
    void testGetActivitiesByUserId() {
        List<Activity> activities = workload.getActivitiesByUserId(sub.userId);
        assertEquals(List.of(subActivity).toString(), activities.toString());
    }

    @Test
    @Order(2)
    void testGetActivityById() {
        Optional<Activity> activity = workload.getActivityById(subActivity.getActivityId());
        if (activity.isEmpty()) fail("Activity not found");
        assertEquals(subActivity.toString(), activity.get().toString());
    }

    @Test
    @Order(3)
    void testAddActivity() throws Exception {
        String activityName = "test activity";
        String activityType = "ATSR";
        String description = "test";
        int responsibleUserId = sub.userId;
        String year = "All Year";
        int duration = 2;
        int noOfInstances = 80;

        workload.addActivity(
                activityName,
                activityType,
                description,
                responsibleUserId,
                year,
                duration,
                noOfInstances
        );

        new Data(userPathname, activityPathname, null);
        assertEquals(2, Data.activityData.getActivities().size());
        Activity activity = Data.activityData.getActivities().get(1);
        assertEquals(activityName, activity.getActivityName());
        assertEquals(activityType, activity.getActivityType());
        assertEquals(description, activity.getDescription());
        assertEquals(responsibleUserId, activity.getResponsibleUserId());
        assertEquals(year, activity.getYear());
        assertEquals(duration, activity.getDuration());
        assertEquals(noOfInstances, activity.getNoOfInstances());
    }

    @Test
    @Order(4)
    void testUpdateActivity() throws Exception {
        String activityName = "test activity1";
        String activityType = "Other";
        String description = "test1";
        int responsibleUserId = sub.userId;
        String year = "Trimester";
        int duration = 3;
        int noOfInstances = 70;

        workload.updateActivity(
                2,
                activityName,
                activityType,
                description,
                responsibleUserId,
                year,
                duration,
                noOfInstances
        );

        new Data(userPathname, activityPathname, null);
        assertEquals(2, Data.activityData.getActivities().size());
        Activity activity = Data.activityData.getActivities().get(1);
        assertEquals(activityName, activity.getActivityName());
        assertEquals(activityType, activity.getActivityType());
        assertEquals(description, activity.getDescription());
        assertEquals(responsibleUserId, activity.getResponsibleUserId());
        assertEquals(year, activity.getYear());
        assertEquals(duration, activity.getDuration());
        assertEquals(noOfInstances, activity.getNoOfInstances());
    }

    @Test
    @Order(5)
    void testDeleteActivity() throws Exception {
        workload.deleteActivity(2);
        new Data(userPathname, activityPathname, null);
        assertEquals(1, Data.activityData.getActivities().size());
    }
}