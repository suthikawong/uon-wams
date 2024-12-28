package com.uon.uonwams.models;

import com.uon.uonwams.data.Data;
import dnl.utils.text.table.TextTable;

import java.util.*;

import static com.uon.uonwams.models.User.getRandomPassword;
import static com.uon.uonwams.models.User.hashPassword;

public class UserManagement {
    private final List<User> users;
    private static User loginUser;

    public UserManagement(User loginUser) {
        UserManagement.loginUser = loginUser;
        this.users = loginUser.getSubordinate();
    }

    public List<User> getUsers() {
        return users;
    }

    public Optional<User> getUserById(int userId) {
        return this.users.stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst();
    }

    public void addUser(int userId, String name, String email, double fteRatio, String subjectArea, Integer lineManagerUserId) {
        try {
            Optional<User> lineManagerUser;
            if (lineManagerUserId != null) {
                lineManagerUser = Data.userData.getUsers().stream().filter(user -> user.getUserId() == lineManagerUserId).findFirst();
                if (lineManagerUser.isEmpty()) {
                    System.out.println("Selected line manager is not exist");
                    return;
                }
            } else {
                lineManagerUser = null;
            }
            String generatedPassword = getRandomPassword();
            Data.userData.insertUser(userId, name, hashPassword(generatedPassword), email, fteRatio, subjectArea, lineManagerUser == null ? null : lineManagerUser.get().getUserId());
            EmailUtil emailUtil = new EmailUtil();
            emailUtil.sendEmail(email,"Temporary password email", "Your temporary password is \"" + generatedPassword + "\" ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot add user: " + e.getMessage());
        }
    }

    public void updateUser(int userId, String name, String email, double fteRatio, String subjectArea, Integer lineManagerUserId) {
        try {
            Optional<User> lineManagerUser;

            Optional<User> updatedUser = Data.userData.getUsers().stream().filter(user -> user.getUserId() == userId).findFirst();
            if (updatedUser.isEmpty()) {
                System.out.println("Selected user is not exist");
                return;
            }

            if (lineManagerUserId != null) {
                lineManagerUser = Data.userData.getUsers().stream().filter(user -> user.getUserId() == lineManagerUserId).findFirst();
                if (lineManagerUser.isEmpty()) {
                    System.out.println("Selected line manager is not exist");
                    return;
                }
            } else {
                lineManagerUser = null;
            }

            Data.userData.updateUser(userId, name, updatedUser.get().getPassword(), email, fteRatio, subjectArea, lineManagerUser == null ? null : lineManagerUser.get().getUserId());
        } catch (Exception e) {
            System.out.println("Cannot update user: " + e.getMessage());
        }
    }

    public void deleteUser(int userId) {
        try {
            Data.userData.deleteUser(userId);
        } catch (Exception e) {
            System.out.println("Cannot delete user: " + e.getMessage());
        }
    }

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
