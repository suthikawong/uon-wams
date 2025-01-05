/**
 Program: UON WAMS Application
 Filename: UserWorkloadAllocationTest.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import com.uon.uonwams.config.ActivityType;
import com.uon.uonwams.data.Data;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.uon.uonwams.models.User.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

class UserWorkloadAllocationTest {
    private static String pathname = "files/test-userworkloadallocation-class.dat";
    private static File file;
    private static DATFile datFile;
    private static User user;
    private static Activity activity1;
    private static Activity activity2;

    @BeforeAll
    public static void setup() throws Exception {
        file = new File(pathname);
        file.createNewFile();
        datFile = new DATFile<Activity>(pathname);

        user = new User(1, "testname", hashPassword("password"), "email", 1, "sucjectarea", null);

        activity1 = new Activity(1, "actname1", ActivityType.ATSR, "test1", user.userId, user.name, "All Year", 2, 100);
        activity2 = new Activity(2, "actname2", ActivityType.SA, "test2", user.userId, user.name, "All Year", 1, 30);
        datFile.insertRecords(List.of(activity1, activity2));

        new Data(null, pathname);

    }

    @AfterAll
    public static void teardown() throws IOException {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testCalculateWorkloadAllocation() {
        UserWorkloadAllocation userWorkloadAllocation = new UserWorkloadAllocation(user);
        assertEquals(470, userWorkloadAllocation.getTotalHours());
        assertEquals(1570, userWorkloadAllocation.getFteHours());
        assertEquals(440, userWorkloadAllocation.getTotalAtsrTs());
        assertEquals(12.8, userWorkloadAllocation.getPercentageOfAtsrAllocated());
        assertEquals(30, userWorkloadAllocation.getParcentageOfTotalHoursAllocated());
        assertEquals(550, userWorkloadAllocation.getFteAtsrHours());
    }
}