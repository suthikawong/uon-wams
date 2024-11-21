package com.uon.uonwams.data;

import com.uon.uonwams.models.CSVFile;
import com.uon.uonwams.models.User;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserData {
    public static final List<User> users = new ArrayList<>();

    public UserData() throws FileNotFoundException {
        CSVFile file = new CSVFile("files/user.csv");
        List<HashMap<String, String>> data = file.getData();
        for (HashMap<String, String> record: data) {
            users.add(parseUser(record));
        }
    }

    public User parseUser(HashMap<String, String> record) {
        return new User(
                Integer.parseInt(record.get("userId")),
                record.get("name"),
                record.get("password"),
                record.get("email")
        );
    }
}
