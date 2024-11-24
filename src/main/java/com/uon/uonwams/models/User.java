package com.uon.uonwams.models;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;

import java.util.LinkedHashMap;

public class User {
    private int userId;
    private String name;
    private String password;
    private String email;
    private static final BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);

//    public static void main(String[] args) {
//        String pass = hashPassword("12345");
//        System.out.println(pass);
//    }

    public User() {}

    public User(int userId, String name, String password, String email) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
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

    public User login(int userId, String password) {
        for (User user: WAMSApplication.userData.getUsers()) {
            if (userId == user.getUserId() & isMatchedPassword(password, user.getPassword())) {
                this.userId = user.getUserId();
                this.name = user.getName();
                this.password = user.getPassword();
                this.email = user.getEmail();
                return this;
            }
        }
        return null;
    }

    public static String hashPassword(String password) {
        // Ref: https://davidbertoldi.medium.com/hashing-passwords-in-java-757e787ce71c
        Hash hash = Password.hash(password)
                .addPepper("shared-secret")
                .with(bcrypt);
        return hash.getResult();
    }

    private boolean isMatchedPassword(String plainPassword, String storedPassword) {
        return Password.check(plainPassword, storedPassword)
                .addPepper("shared-secret")
                .with(bcrypt);
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
