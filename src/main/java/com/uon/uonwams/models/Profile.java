package com.uon.uonwams.models;

import com.uon.uonwams.data.UserData;

import static com.uon.uonwams.models.User.hashPassword;

public class Profile {
    private final User user;
    private final UserData userData;

    public Profile(User user) {
        this.user = user;
        this.userData = WAMSApplication.userData;
    }

    public User changePassword(String password) {
        return userData.updateUser(user.getUserId(), user.getName(), hashPassword(password), user.getEmail());
    }
}
