package com.uon.uonwams.data;

import com.uon.uonwams.models.CSVFile;
import com.uon.uonwams.models.User;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class UserData {
    public static final List<User> users = new ArrayList<>();

    public UserData() {
        CSVFile file = new CSVFile("files/user.csv");
        List<LinkedHashMap<String, String>> data = file.getData();
        for (LinkedHashMap<String, String> record: data) {
            users.add(parseUser(record));
        }
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
