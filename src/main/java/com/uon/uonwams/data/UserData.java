package com.uon.uonwams.data;

import com.uon.uonwams.models.DATFile;
import com.uon.uonwams.models.User;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class UserData {
    private final DATFile file;
    private List<User> users;

    public UserData(String pathname) {
        this.file = new DATFile<User>(pathname);
        this.users = file.getData();
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void insertUser(int userId, String name, String password, String email, double fteRatio, String subjectArea, Integer lineManagerUserId) throws Exception {
        User user = new User(userId, name, password, email, fteRatio, subjectArea, lineManagerUserId);
        file.insertRecord(user);
        this.users = file.getData();
    }

    public void updateUser(int userId, String name, String password, String email, double fteRatio, String subjectArea, Integer lineManagerUserId) throws Exception {
        User user = new User(userId, name, password, email, fteRatio, subjectArea, lineManagerUserId);
        file.updateRecord(user);
        this.users = file.getData();
    }

    public void deleteUser(int userId) throws Exception {
        file.deleteRecord(userId);
        this.users = file.getData();
    }

}
