package com.uon.uonwams.data;

import com.uon.uonwams.config.ContractType;
import com.uon.uonwams.models.Activity;
import com.uon.uonwams.models.CSVFile;
import com.uon.uonwams.models.User;
import org.apache.commons.beanutils.ConversionException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UserData {
    private final CSVFile file;
    private List<User> users = new ArrayList<>();

    public UserData() {
        this.file = new CSVFile("files/user.csv");
        List<LinkedHashMap<String, String>> data = file.getData();
        try {
            for (LinkedHashMap<String, String> record: data) {
                users.add(parseUser(record));
            }
        } catch (Exception e) {
            System.out.println("Cannot read file: " + e.getMessage());
            throw e;
        }

    }

    public User updateUser(int userId, String name, String password, String email, ContractType contractType, String subjectArea, int lineManagerUserId) {
        User user = new User(userId, name, password, email, contractType, subjectArea, lineManagerUserId);
        file.updateRecord(user.toHashMap(), "userId");
        List<LinkedHashMap<String, String>> data = file.getData();
        this.users = parseUsers(data);
        return user;
    }

    public List<User> parseUsers(List<LinkedHashMap<String, String>> records) {
        List<User> users = new ArrayList<>();
        for (LinkedHashMap<String, String> record: records) {
            users.add(parseUser(record));
        }
        return users;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public User parseUser(LinkedHashMap<String, String> record) {
        ContractType contractType;
        if (record.get("contractType").equals(ContractType.FULL_TIME.label)) {
            contractType = ContractType.FULL_TIME;
        } else if (record.get("contractType").equals(ContractType.PART_TIME_1_0.label)) {
            contractType = ContractType.PART_TIME_1_0;
        } else if (record.get("contractType").equals(ContractType.PART_TIME_0_5.label)) {
            contractType = ContractType.PART_TIME_0_5;
        } else {
            throw new ConversionException("Invalid contract type in file");
        }
        return new User(
                Integer.parseInt(record.get("userId")),
                record.get("name"),
                record.get("password"),
                record.get("email"),
                contractType,
                record.get("subjectArea"),
                record.get("lineManagerUserId").isEmpty() ? null : Integer.parseInt(record.get("lineManagerUserId"))
        );
    }
}
