package com.uon.uonwams.data;

import com.uon.uonwams.models.CSVFile;
import com.uon.uonwams.models.DATFile;
import com.uon.uonwams.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class UserData {
    private final DATFile file;
    private List<User> users = new ArrayList<>();

    public UserData() {
        this.file = new DATFile<User>("files/user.dat");
        this.users = file.getData();
    }

    public List<User> getUsers() {
        return this.users;
    }

    public User insertUser(int userId, String name, String password, String email, float fteRatio, String subjectArea, Integer lineManagerUserId) throws Exception {
        User user = new User(userId, name, password, email, fteRatio, subjectArea, lineManagerUserId);
        file.insertRecord(user);
        this.users = file.getData();
        return user;
    }

    public User updateUser(int userId, String name, String password, String email, float fteRatio, String subjectArea, Integer lineManagerUserId) throws Exception {
        User user = new User(userId, name, password, email, fteRatio, subjectArea, lineManagerUserId);
        file.updateRecord(user);
        this.users = file.getData();
        return user;
    }

    public int deleteUser(int userId) throws Exception {
        file.deleteRecord(userId);
        this.users = file.getData();
        return userId;
    }

//    public List<User> parseUsers(List<LinkedHashMap<String, String>> records) throws Exception {
//        List<User> users = new ArrayList<>();
//        for (LinkedHashMap<String, String> record: records) {
//            users.add(parseUser(record));
//        }
//        return users;
//    }
//
//    public User parseUser(LinkedHashMap<String, String> record) throws Exception {
//        return new User(
//                Integer.parseInt(record.get("userId")),
//                record.get("name"),
//                record.get("password"),
//                record.get("email"),
//                Float.parseFloat(record.get("fteRatio")),
//                record.get("subjectArea"),
//                record.get("lineManagerUserId").isEmpty() ? null : Integer.parseInt(record.get("lineManagerUserId"))
//        );
//    }
}
