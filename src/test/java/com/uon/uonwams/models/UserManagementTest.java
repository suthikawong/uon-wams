package com.uon.uonwams.models;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.uon.uonwams.models.User.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserManagementTest {
    private static String pathname = "files/test-usermanagement-class.dat";
    private static File file;
    private static DATFile datFile;
    private static User lineManager;
    private static User sub;
    private static UserManagement um;


    @BeforeAll
    public static void setup() throws Exception {
        file = new File(pathname);
        file.createNewFile();
        datFile = new DATFile<User>(pathname);

        lineManager = new User(1, "testname", hashPassword("password"), "email", 1, "sucjectarea", null);
        sub = new User(2, "testname", hashPassword("password"), "email", 1, "sucjectarea", 1);
        datFile.insertRecords(List.of(lineManager, sub));

//        lineManager = new User(1, "Suthika Wongsiridech", hashPassword("12345"), "suthika.dev@gmail.com", 1, "Computing", null);
//        datFile.insertRecord(lineManager);

        new WAMSApplication(pathname, null);
        um = new UserManagement(lineManager);
    }

    @AfterAll
    public static void teardown() throws IOException {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @Order(1)
    void testGetUsers() {
        assertEquals(List.of(sub).toString(), um.getUsers().toString());
    }

    @Test
    @Order(2)
    void testGetUserById() {
        Optional<User> user = um.getUserById(sub.getUserId());
        if (user.isEmpty()) fail("User not found");
        assertEquals(sub.toString(), user.get().toString());
    }

    @Test
    @Order(3)
    void testAddUser() throws Exception {
        int userId = 3;
        String name = "addname";
        String email = "addemail";
        double fteRatio = 1;
        String subjectArea = "addsubjectarea";
        int lineManagerUserId = 1;

        um.addUser(
                userId,
                name,
                email,
                fteRatio,
                subjectArea,
                lineManagerUserId
        );

        new WAMSApplication(pathname, null);
        assertEquals(3, WAMSApplication.userData.getUsers().size());
        User user = WAMSApplication.userData.getUsers().get(2);
        assertEquals(userId, user.userId);
        assertEquals(name, user.name);
        assertEquals(email, user.email);
        assertEquals(fteRatio, user.fteRatio);
        assertEquals(subjectArea, user.subjectArea);
        assertEquals(lineManagerUserId, user.lineManagerUserId);
    }

    @Test
    @Order(4)
    void testUpdateUser() throws Exception {
        int userId = 3;
        String name = "updatename";
        String email = "updateemail";
        double fteRatio = 1;
        String subjectArea = "updatesubjectarea";
        int lineManagerUserId = 1;

        um.updateUser(
                userId,
                name,
                email,
                fteRatio,
                subjectArea,
                lineManagerUserId
        );

        new WAMSApplication(pathname, null);
        assertEquals(3, WAMSApplication.userData.getUsers().size());
        User user = WAMSApplication.userData.getUsers().get(2);
        assertEquals(userId, user.userId);
        assertEquals(name, user.name);
        assertEquals(email, user.email);
        assertEquals(fteRatio, user.fteRatio);
        assertEquals(subjectArea, user.subjectArea);
        assertEquals(lineManagerUserId, user.lineManagerUserId);
    }

    @Test
    @Order(5)
    void testDeleteUser() throws Exception {
        um.deleteUser(3);
        new WAMSApplication(pathname, null);
        assertEquals(2, WAMSApplication.userData.getUsers().size());
    }
}