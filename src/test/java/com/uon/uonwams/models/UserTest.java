package com.uon.uonwams.models;

import com.uon.uonwams.data.Data;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.uon.uonwams.models.User.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static String pathname = "files/test-user-class.dat";
    private static File file;
    private static DATFile datFile;
    private static User lineManager;
    private static User sub;

    @BeforeAll
    public static void setup() throws Exception {
        file = new File(pathname);
        file.createNewFile();
        datFile = new DATFile<User>(pathname);
        lineManager = new User(1, "testname", hashPassword("password"), "email", 1, "sucjectarea", null);
        sub = new User(2, "testname", hashPassword("password"), "email", 1, "sucjectarea", 1);
        datFile.insertRecords(List.of(lineManager, sub));
        new Data(pathname, null);
    }

    @AfterAll
    public static void teardown() throws IOException {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testLogin() {
        User user = new User();
        user.login(1, "password");
        assertEquals(lineManager.userId, user.userId);
        assertEquals(lineManager.name, user.name);
        assertEquals(lineManager.password, user.password);
        assertEquals(lineManager.email, user.email);
        assertEquals(lineManager.fteRatio, user.fteRatio);
        assertEquals(lineManager.subjectArea, user.subjectArea);
        assertEquals(lineManager.lineManagerUserId, user.lineManagerUserId);
    }

    @Test
    void testGetSubordinate() {
        assertEquals(List.of(sub).toString(), lineManager.getSubordinate().toString());
    }

    @Test
    void testCheckIsLineManager() {
        assertEquals(true, lineManager.checkIsLineManager());
    }

    @Test
    void testCheckIsStaff() {
        assertEquals(false, sub.checkIsLineManager());
    }
}