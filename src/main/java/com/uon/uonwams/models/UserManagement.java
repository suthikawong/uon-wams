package com.uon.uonwams.models;

import com.uon.uonwams.data.UserData;
import dnl.utils.text.table.TextTable;

import java.util.*;

import static com.uon.uonwams.models.User.hashPassword;

public class UserManagement {
    private List<User> users = new ArrayList<>();
    private static User loginUser;

    public UserManagement(User loginUser) {
//        this.userData = WAMSApplication.userData;
//        UserManagement.loginUser = loginUser;
//
//        for (User user: userData.getUsers()) {
//            if (user.getLineManagerUserId() != null && user.getLineManagerUserId() == loginUser.getUserId()) {
//                this.users.add(user);
//            }
//        }
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

    public User addUser(int userId, String name, String email, float fteRatio, String subjectArea, Integer lineManagerUserId) {
        try {
            Optional<User> lineManagerUser;
            if (lineManagerUserId != null) {
                lineManagerUser = WAMSApplication.userData.getUsers().stream().filter(user -> user.getUserId() == lineManagerUserId).findFirst();
                if (lineManagerUser.isEmpty()) {
                    System.out.println("Selected line manager is not exist");
                    return null;
                }
            } else {
                lineManagerUser = null;
            }

            String generatedPassword = User.hashPassword("password");
            return WAMSApplication.userData.insertUser(userId, name, generatedPassword, email, fteRatio, subjectArea, lineManagerUser == null ? null : lineManagerUser.get().getUserId());
        } catch (Exception e) {
            System.out.println("Cannot add user: " + e.getMessage());
        }
        return null;
    }

    public User updateUser(int userId, String name, String email, float fteRatio, String subjectArea, Integer lineManagerUserId) {
        try {
            Optional<User> lineManagerUser;

            Optional<User> updatedUser = WAMSApplication.userData.getUsers().stream().filter(user -> user.getUserId() == userId).findFirst();
            if (updatedUser.isEmpty()) {
                System.out.println("Selected user is not exist");
                return null;
            }

            if (lineManagerUserId != null) {
                lineManagerUser = WAMSApplication.userData.getUsers().stream().filter(user -> user.getUserId() == lineManagerUserId).findFirst();
                if (lineManagerUser.isEmpty()) {
                    System.out.println("Selected line manager is not exist");
                    return null;
                }
            } else {
                lineManagerUser = null;
            }

            return WAMSApplication.userData.updateUser(userId, name, updatedUser.get().getPassword(), email, fteRatio, subjectArea, lineManagerUser == null ? null : lineManagerUser.get().getUserId());
        } catch (Exception e) {
            System.out.println("Cannot update user: " + e.getMessage());
        }
        return null;
    }

    public Integer deleteUser(int userId) {
        try {
            return WAMSApplication.userData.deleteUser(userId);
        } catch (Exception e) {
            System.out.println("Cannot delete user: " + e.getMessage());
        }
        return null;
    }

    public void changePassword(String password) throws Exception {
        WAMSApplication.userData.updateUser(loginUser.getUserId(), loginUser.getName(), hashPassword(password), loginUser.getEmail(), loginUser.getFteRatio(), loginUser.getSubjectArea(), loginUser.getLineManagerUserId());
    }

    public String forgotPassword(User user) throws Exception {
        String password = getRandomPassword();
        WAMSApplication.userData.updateUser(user.getUserId(), user.getName(), hashPassword(password), user.getEmail(), user.getFteRatio(), user.getSubjectArea(), user.getLineManagerUserId());
        return password;
    }

    public static String getRandomPassword() {
        // https://stackoverflow.com/questions/51322750/generate-6-digit-random-number
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
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
