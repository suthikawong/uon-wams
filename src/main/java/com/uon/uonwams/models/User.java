package com.uon.uonwams.models;

import java.util.LinkedHashMap;

public class User {
    private final int userId;
    private final String name;
    private final String password;
    private final String email;

//    public static void main(String[] args) {
//        String pass = hashPassword("12345");
//        System.out.println(pass);
//    }


    public User(int userId, String name, String password, String email) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public void changePassword(int userId, String password) {

    }

    public void updateUser(int userId, String password) {

    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LinkedHashMap<String, String> toHashMap() {
        LinkedHashMap<String, String> mapUser = new LinkedHashMap<String, String>();
        mapUser.put("userId", Integer.toString(this.userId));
        mapUser.put("name", this.name);
        mapUser.put("password", this.password);
        mapUser.put("email", this.email);
        return mapUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
