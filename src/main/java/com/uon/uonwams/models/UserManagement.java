package com.uon.uonwams.models;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserManagement {
    public List<User> users = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        new UserManagement();
    }

    public UserManagement() throws FileNotFoundException {
        CSVFile file = new CSVFile("files/user.csv");
        List<HashMap<String, String>> data = file.getData();
        for (HashMap<String, String> record: data) {
            this.users.add(new User(Integer.parseInt(record.get("userId")), record.get("name"), record.get("password"), record.get("email")));
        }
        System.out.println(this.users);
    }



}
