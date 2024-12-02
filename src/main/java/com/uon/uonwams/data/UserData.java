package com.uon.uonwams.data;

import com.uon.uonwams.models.CSVFile;
import com.uon.uonwams.models.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UserData {
    private final CSVFile file;
    private List<User> users = new ArrayList<>();

    public UserData() throws Exception {
        this.file = new CSVFile("files/user.csv");
        List<LinkedHashMap<String, String>> data = file.getData();
        try {
            for (LinkedHashMap<String, String> record: data) {
                users.add(parseUser(record));
            }
        } catch (Exception e) {
            System.out.println("Cannot read file: " + e.getMessage());
            throw e;
        }

    }

    public List<User> getUsers() {
        return this.users;
    }

    public List<String> getAttributes() {
        return this.file.getHeader();
    }

    public User insertUser(int userId, String name, String password, String email, float fteRatio, String subjectArea, Integer lineManagerUserId) throws Exception {
        User user = new User(userId, name, password, email, fteRatio, subjectArea, lineManagerUserId);
        file.insertRecord(user.toHashMap());
        List<LinkedHashMap<String, String>> data = file.getData();
        this.users = parseUsers(data);
        return user;
    }

    public User updateUser(int userId, String name, String password, String email, float fteRatio, String subjectArea, Integer lineManagerUserId) throws Exception {
        User user = new User(userId, name, password, email, fteRatio, subjectArea, lineManagerUserId);
        file.updateRecord(user.toHashMap(), "userId");
        List<LinkedHashMap<String, String>> data = file.getData();
        this.users = parseUsers(data);
        return user;
    }

    public int deleteUser(int userId) throws Exception {
        file.deleteRecord(userId);
        List<LinkedHashMap<String, String>> data = file.getData();
        this.users = parseUsers(data);
        return userId;
    }

    public List<User> parseUsers(List<LinkedHashMap<String, String>> records) throws Exception {
        List<User> users = new ArrayList<>();
        for (LinkedHashMap<String, String> record: records) {
            users.add(parseUser(record));
        }
        return users;
    }

    public User parseUser(LinkedHashMap<String, String> record) throws Exception {
        return new User(
                Integer.parseInt(record.get("userId")),
                record.get("name"),
                record.get("password"),
                record.get("email"),
                Float.parseFloat(record.get("fteRatio")),
                record.get("subjectArea"),
                record.get("lineManagerUserId").isEmpty() ? null : Integer.parseInt(record.get("lineManagerUserId"))
        );
    }
}
