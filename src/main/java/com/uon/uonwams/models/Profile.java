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
        try {
            return userData.updateUser(user.getUserId(), user.getName(), hashPassword(password), user.getEmail(), user.getContractType(), user.getSubjectArea(), user.getLineManagerUserId());
        } catch (Exception e) {
            System.out.println("Fail to change password, please try again");
        }
        return null;
    }
}
