package com.uon.uonwams.models;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;

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

    public User(int userId, String name, String password, String email) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public void login(int userId, String password) {

    }

    public void changePassword(int userId, String password) {

    }

    private static String hashPassword(String password) {
        // Ref: https://davidbertoldi.medium.com/hashing-passwords-in-java-757e787ce71c
        Hash hash = Password.hash(password)
                .addPepper("shared-secret")
                .with(bcrypt);
        return hash.getResult();
    }

    private boolean isMatchedPassword(String password) {
        return Password.check(password, "asdf")
                .addPepper("shared-secret")
                .with(bcrypt);
    }

    public void getUser(int userId, String password) {

    }

    public void updateUser(int userId, String password) {

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
