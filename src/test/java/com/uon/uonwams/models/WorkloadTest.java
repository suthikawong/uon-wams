package com.uon.uonwams.models;

import com.uon.uonwams.config.ActivityType;
import com.uon.uonwams.data.Data;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.uon.uonwams.models.User.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WorkloadTest {
    private static String userPathname = "files/test-workload-user-class.dat";
    private static String activityPathname = "files/test-workload-activity-class.dat";
    private static File userFile;
    private static File activityFile;
    private static DATFile userDatFile;
    private static DATFile activityDatFile;
    private static User lineManager;
    private static User sub;
    private static Activity subActivity;
    private static Workload workload;

    @BeforeAll
    public static void setup() throws Exception {
        userFile = new File(userPathname);
        userFile.createNewFile();
        userDatFile = new DATFile<User>(userPathname);

        lineManager = new User(1, "testname", hashPassword("password"), "email", 1, "sucjectarea", null);
        sub = new User(2, "testname", hashPassword("password"), "email", 1, "sucjectarea", 1);
        userDatFile.insertRecords(List.of(lineManager, sub));


        activityFile = new File(activityPathname);
        activityFile.createNewFile();
        activityDatFile = new DATFile<User>(activityPathname);

        subActivity = new Activity(1, "activityname", ActivityType.ATSR, "test", sub.userId, sub.name, "All Year", 2, 80);
        activityDatFile.insertRecord(subActivity);

        new Data(userPathname, activityPathname);
        workload = new Workload(lineManager);
    }

    @AfterAll
    public static void teardown() throws IOException {
        if (userFile.exists()) {
            userFile.delete();
        }
        if (activityFile.exists()) {
            activityFile.delete();
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
        ActivityType activityType = ActivityType.ATSR;
        String description = "test";
        int responsibleUserId = sub.userId;
        String year = "All Year";
        int duration = 2;
        int noOfInstances = 80;

        workload.addActivity(
                activityName,
                activityType.label,
                description,
                responsibleUserId,
                year,
                duration,
                noOfInstances
        );

        new Data(userPathname, activityPathname);
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
        ActivityType activityType = ActivityType.OTHER;
        String description = "test1";
        int responsibleUserId = sub.userId;
        String year = "Trimester";
        int duration = 3;
        int noOfInstances = 70;

        workload.updateActivity(
                2,
                activityName,
                activityType.label,
                description,
                responsibleUserId,
                year,
                duration,
                noOfInstances
        );

        new Data(userPathname, activityPathname);
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
        new Data(userPathname, activityPathname);
        assertEquals(1, Data.activityData.getActivities().size());
    }
}