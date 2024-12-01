package com.uon.uonwams.models;

import com.uon.uonwams.config.ContractType;
import com.uon.uonwams.data.UserData;
import dnl.utils.text.table.TextTable;
import org.apache.commons.beanutils.ConversionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserManagement {
    private List<User> users = new ArrayList<>();
    private static User loginUser;
    private UserData userData;

    public UserManagement(User loginUser) {
        this.userData = WAMSApplication.userData;
        UserManagement.loginUser = loginUser;

        for (User user: userData.getUsers()) {
            if (user.getLineManagerUserId() != null && user.getLineManagerUserId() == loginUser.getUserId()) {
                this.users.add(user);
            }
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public Optional<User> getUserById(int userId) {
        return this.users.stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst();
    }

    public User addUser(int userId, String name, String email, String type, String subjectArea, Integer lineManagerUserId) {
        try {
            ContractType contractType = convertStringToContractType(type);
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
            return userData.insertUser(userId, name, generatedPassword, email, contractType, subjectArea, lineManagerUser == null ? null : lineManagerUser.get().getUserId());
        } catch (Exception e) {
            System.out.println("Cannot add user: " + e.getMessage());
        }
        return null;
    }

    public User updateUser(int userId, String name, String email, String contractType, String subjectArea, Integer lineManagerUserId) {
        try {
            ContractType type = convertStringToContractType(contractType);
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

            return userData.updateUser(userId, name, updatedUser.get().getPassword(), email, type, subjectArea, lineManagerUser == null ? null : lineManagerUser.get().getUserId());
        } catch (Exception e) {
            System.out.println("Cannot update user: " + e.getMessage());
        }
        return null;
    }

    public Integer deleteUser(int userId) {
        try {
            return userData.deleteUser(userId);
        } catch (Exception e) {
            System.out.println("Cannot delete user: " + e.getMessage());
        }
        return null;
    }

    public void logUsers() {
        List<String> displayColumns = new ArrayList<>(this.userData.getAttributes());
        displayColumns.add("lineManagerUserName");
        displayColumns = displayColumns.stream().filter(column -> !column.equals("password")).toList();
        TextTable tt = new TextTable(displayColumns.toArray(new String[0]),convertUserListToArray(this.users));
        tt.printTable();
        System.out.println("_________________________________________________________________________________________________________________");
        System.out.println();
    }

    private ContractType convertStringToContractType(String contractType) {
        if (contractType.equalsIgnoreCase(ContractType.FULL_TIME.label)) {
            return ContractType.FULL_TIME;
        } else if (contractType.equalsIgnoreCase(ContractType.PART_TIME_1_0.label)) {
            return ContractType.PART_TIME_1_0;
        } else if (contractType.equalsIgnoreCase(ContractType.PART_TIME_0_5.label)) {
            return ContractType.PART_TIME_0_5;
        } else {
            throw new ConversionException("Invalid contract type");
        }
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
            array[i][3] = user.getContractType().label;
            array[i][4] = user.getSubjectArea();
            array[i][5] = user.getLineManagerUserId();
            array[i][6] = loginUser.getName();
        }
        return array;
    }
}
