/**
 Program: UON WAMS Application
 Filename: UserManagement.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import com.uon.uonwams.data.Data;
import dnl.utils.text.table.TextTable;

import java.util.*;

import static com.uon.uonwams.models.User.getRandomPassword;
import static com.uon.uonwams.models.User.hashPassword;

public class UserManagement {
    private final List<User> users; // store subordinates that will be display in workload page
    private static User loginUser; // store logged-in user

    public UserManagement(User loginUser) {
        UserManagement.loginUser = loginUser;
        this.users = loginUser.getSubordinate();
    }

    public List<User> getUsers() {
        return users;
    }

    // find user by userId
    public Optional<User> getUserById(int userId) {
        return this.users.stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst();
    }

    // add a new user to the system
    public void addUser(int userId, String name, String email, double fteRatio, String subjectArea, Integer lineManagerUserId) throws Exception {
        Optional<User> lineManagerUser;
        // if lineManagerUserId was specified, check whether this lineManagerUserId in exists in the system
        if (lineManagerUserId != null) {
            lineManagerUser = Data.userData.getUsers().stream().filter(user -> user.getUserId() == lineManagerUserId).findFirst();
            if (lineManagerUser.isEmpty()) {
                System.out.println("Selected line manager is not exist");
                return;
            }
        } else {
            lineManagerUser = null;
        }
        // generate temporary password for the new user
        String generatedPassword = getRandomPassword();

        try {
            EmailUtil emailUtil = new EmailUtil();
            // send temporary password to the new user via email
            emailUtil.sendEmail(email,"Temporary password email", "Your temporary password is \"" + generatedPassword + "\" ");
        } catch (Exception e) {
            // if any error occur, don't add this user to the system
            throw new Exception("Email is invalid.");
        }
        // add user
        Data.userData.insertUser(userId, name, hashPassword(generatedPassword), email, fteRatio, subjectArea, lineManagerUser == null ? null : lineManagerUser.get().getUserId());
    }

    // update a specific user in the system
    public void updateUser(int userId, String name, String email, double fteRatio, String subjectArea, Integer lineManagerUserId) {
        try {
            Optional<User> lineManagerUser;

            // check whether this user exists
            Optional<User> updatedUser = Data.userData.getUsers().stream().filter(user -> user.getUserId() == userId).findFirst();
            if (updatedUser.isEmpty()) {
                System.out.println("Selected user is not exist");
                return;
            }

            // if lineManagerUserId was specified, check whether this lineManagerUserId in exists in the system
            if (lineManagerUserId != null) {
                lineManagerUser = Data.userData.getUsers().stream().filter(user -> user.getUserId() == lineManagerUserId).findFirst();
                if (lineManagerUser.isEmpty()) {
                    System.out.println("Selected line manager is not exist");
                    return;
                }
            } else {
                lineManagerUser = null;
            }
            // update user
            Data.userData.updateUser(userId, name, updatedUser.get().getPassword(), email, fteRatio, subjectArea, lineManagerUser == null ? null : lineManagerUser.get().getUserId());
        } catch (Exception e) {
            System.out.println("Cannot update user: " + e.getMessage());
        }
    }

    // delete a specific user from the system
    public void deleteUser(int userId) {
        try {
            // delete user
            Data.userData.deleteUser(userId);
        } catch (Exception e) {
            System.out.println("Cannot delete user: " + e.getMessage());
        }
    }

    // display the subordinate staffs in table format (console application)
    public void logUsers() {
        List<String> displayColumns = Arrays.asList("userId", "name", "password", "email", "fteRatio", "subjectArea", "lineManagerUserId", "lineManagerUserName");
        TextTable tt = new TextTable(displayColumns.toArray(new String[0]),convertUserListToArray(this.users));
        tt.printTable();
        System.out.println();
    }

    public static Object[][] convertUserListToArray(List<User> list) {
        int rows = list.size();
        int columns = 7;
        Object[][] array = new Object[rows][columns];

        for (int i = 0; i < rows; i++) {
            User user = list.get(i);
            array[i][0] = user.getUserId();
            array[i][1] = user.getName();
            array[i][2] = user.getEmail();
            array[i][3] = user.getFteRatio();
            array[i][4] = user.getSubjectArea();
            array[i][5] = user.getLineManagerUserId();
            array[i][6] = loginUser.getName();
        }
        return array;
    }
}
