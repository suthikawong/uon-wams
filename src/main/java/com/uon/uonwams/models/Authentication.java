package com.uon.uonwams.models;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import com.uon.uonwams.data.UserData;

import java.util.List;

public class Authentication {

    private static final BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);

    public User login(int userId, String password) {
        for (User user: UserData.users) {
            if (userId == user.getUserId() & isMatchedPassword(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    private static String hashPassword(String password) {
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
}
