package com.uon.uonwams.models;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DATFileTest {
    private static String pathname = "files/test-datfile-class.dat";
    private static File file;
    private static DATFile datFile;

    @BeforeAll
    public static void setup() throws IOException {
        file = new File(pathname);
        file.createNewFile();
    }

    @AfterAll
    public static void teardown() throws IOException {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @Order(1)
    void testReadRecord() {
        datFile = new DATFile<User>(pathname);
        assertEquals(0, datFile.getData().size());
    }

    @Test
    @Order(2)
    void testInsertRecord() {
        User user = new User(1, "testname", "password", "email", 1, "sucjectarea", 1);
        datFile.insertRecord(user);
        List<User> list = datFile.getData();
        assertEquals(1, list.size());
        User data = list.getFirst();
        assertEquals(1, data.getUserId());
        assertEquals("testname", data.getName());
        assertEquals("password", data.getPassword());
        assertEquals("email", data.getEmail());
        assertEquals(1, data.getFteRatio());
        assertEquals("sucjectarea", data.getSubjectArea());
        assertEquals(1, data.getLineManagerUserId());
    }

    @Test
    @Order(3)
    void testInsertRecords() {
        User user1 = new User(2, "testname", "password", "email", 1, "sucjectarea", 1);
        User user2 = new User(3, "testname", "password", "email", 1, "sucjectarea", 1);
        datFile.insertRecords(List.of(user1, user2));
        List<User> list = datFile.getData();
        assertEquals(3, list.size());
    }

    @Test
    @Order(3)
    void testUpdateRecord() {
        User user = new User(1, "testname1", "password1", "email1", 0.5, "sucjectarea1", 2);
        datFile.updateRecord(user);
        List<User> list = datFile.getData();
        assertEquals(3, list.size());
        User data = list.getFirst();
        assertEquals(1, data.getUserId());
        assertEquals("testname1", data.getName());
        assertEquals("password1", data.getPassword());
        assertEquals("email1", data.getEmail());
        assertEquals(0.5, Math.round(data.getFteRatio()), 1);
        assertEquals("sucjectarea1", data.getSubjectArea());
        assertEquals(2, data.getLineManagerUserId());
    }

    @Test
    @Order(4)
    void testDeleteRecord() {
        datFile.deleteRecord(1);
        List<User> list = datFile.getData();
        assertEquals(2, list.size());
    }
}