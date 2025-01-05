package com.uon.uonwams.data;

import com.uon.uonwams.models.DATFile;
import com.uon.uonwams.models.User;

import java.util.List;

public class UserData {
    private final DATFile file;
    private List<User> users;

    // initialize DATFile instance and read data from that file
    public UserData(String pathname) {
        this.file = new DATFile<User>(pathname);
        this.users = file.getData();
    }

    public List<User> getUsers() {
        return this.users;
    }

    // insert a new user into DAT file
    public void insertUser(int userId, String name, String password, String email, double fteRatio, String subjectArea, Integer lineManagerUserId) {
        User user = new User(userId, name, password, email, fteRatio, subjectArea, lineManagerUserId);
        // insert a new user
        file.insertRecord(user);
        // reload latest data and store them into this.users
        this.users = file.getData();
    }

    // update user in the DAT file
    public void updateUser(int userId, String name, String password, String email, double fteRatio, String subjectArea, Integer lineManagerUserId) {
        User user = new User(userId, name, password, email, fteRatio, subjectArea, lineManagerUserId);
        // update user
        file.updateRecord(user);
        // reload latest data and store them into this.users
        this.users = file.getData();
    }

    // delete user from the DAT file
    public void deleteUser(int userId) {
        // delete user
        file.deleteRecord(userId);
        // reload latest data and store them into this.users
        this.users = file.getData();
    }

}
