package com.uon.uonwams.data;

import com.uon.uonwams.models.Activity;
import com.uon.uonwams.models.CSVFile;
import com.uon.uonwams.models.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UserData {
    private final CSVFile file;
    private List<User> users = new ArrayList<>();

    public UserData() {
        this.file = new CSVFile("files/user.csv");
        List<LinkedHashMap<String, String>> data = file.getData();
        for (LinkedHashMap<String, String> record: data) {
            users.add(parseUser(record));
        }
    }

    public User updateUser(int userId, String name, String password, String email) {
        User user = new User(userId, name, password, email);
        file.updateRecord(user.toHashMap(), "userId");
        List<LinkedHashMap<String, String>> data = file.getData();
        this.users = parseUsers(data);
        return user;
    }

    public List<User> parseUsers(List<LinkedHashMap<String, String>> records) {
        List<User> users = new ArrayList<>();
        for (LinkedHashMap<String, String> record: records) {
            users.add(parseUser(record));
        }
        return users;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public User parseUser(LinkedHashMap<String, String> record) {
        return new User(
                Integer.parseInt(record.get("userId")),
                record.get("name"),
                record.get("password"),
                record.get("email")
        );
    }
}
