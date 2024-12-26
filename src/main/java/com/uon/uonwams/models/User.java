package com.uon.uonwams.models;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class User extends DATFileStructure implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int userId;
    protected String name;
    protected String password;
    protected String email;
    protected double fteRatio;
    protected String subjectArea;
    protected Integer lineManagerUserId = null;
    protected boolean isAdmin = false;
    private static final BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);

    public User() {}

    public User(int userId, String name, String password, String email, double fteRatio, String subjectArea, Integer lineManagerUserId) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.fteRatio = fteRatio;
        this.subjectArea = subjectArea;
        this.lineManagerUserId = lineManagerUserId;
    }

    public int getId() {
        return userId;
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

    public double getFteRatio() {
        return fteRatio;
    }

    public String getSubjectArea() {
        return subjectArea;
    }

    public Integer getLineManagerUserId() {
        return lineManagerUserId;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public List<User> getSubordinate() {
        if (this.isAdmin) {
            return new ArrayList<>(WAMSApplication.userData.getUsers());
        }
        List<User> subordinateList = new ArrayList<>();
        for (User user: WAMSApplication.userData.getUsers()) {
            if (((Integer) this.userId).equals(user.getLineManagerUserId())) {
                subordinateList.add(user);
            }
        }
        return subordinateList;
    }

    public boolean checkIsLineManager() {
        for (User user: WAMSApplication.userData.getUsers()) {
            if (((Integer) this.userId).equals(user.getLineManagerUserId())) {
                return true;
            }
        }
        return false;
    }

    public User login(int userId, String password) {
        Dotenv dotenv = Dotenv.load();
        String enableAdminUser = dotenv.get("ENABLE_ADMIN_USER");

        if (enableAdminUser.equals("true")) {
            int adminUserId;
            String adminPassword = dotenv.get("ADMIN_PASSWORD");

            try {
                adminUserId = Integer.parseInt(dotenv.get("ADMIN_USER_ID"));
            } catch(Exception e) {
                System.out.println("Invalid ADMIN_USER_ID");
                System.exit(0);
                return null;
            }

            if (adminUserId == userId && adminPassword.equals(password)) {
                this.userId = userId;
                this.name = "Admin";
                this.isAdmin = true;
                return this;
            }
        }

        for (User user: WAMSApplication.userData.getUsers()) {
            if (userId == user.getUserId() & isMatchedPassword(password, user.getPassword())) {
                this.userId = user.getUserId();
                this.name = user.getName();
                this.password = user.getPassword();
                this.email = user.getEmail();
                this.fteRatio = user.getFteRatio();
                this.subjectArea = user.getSubjectArea();
                this.lineManagerUserId = user.getLineManagerUserId();
                return this;
            }
        }
        return null;
    }

    public static String hashPassword(String password) {
        // Ref: https://davidbertoldi.medium.com/hashing-passwords-in-java-757e787ce71c
        Dotenv dotenv = Dotenv.load();
        String secret = dotenv.get("PASSWORD_SECRET");
        Hash hash = Password.hash(password)
                .addPepper(secret)
                .with(bcrypt);
        return hash.getResult();
    }

    private boolean isMatchedPassword(String plainPassword, String storedPassword) {
        Dotenv dotenv = Dotenv.load();
        String secret = dotenv.get("PASSWORD_SECRET");
        return Password.check(plainPassword, storedPassword)
                .addPepper(secret)
                .with(bcrypt);
    }

    public LinkedHashMap<String, String> toHashMap() {
        LinkedHashMap<String, String> mapUser = new LinkedHashMap<String, String>();
        mapUser.put("userId", Integer.toString(this.userId));
        mapUser.put("name", this.name);
        mapUser.put("password", this.password);
        mapUser.put("email", this.email);
        mapUser.put("fteRatio", Double.toString(this.fteRatio));
        mapUser.put("subjectArea", this.subjectArea);
        mapUser.put("lineManagerUserId", this.lineManagerUserId == null ? "" : Integer.toString(this.lineManagerUserId));
        return mapUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", fteRatio=" + fteRatio +
                ", subjectArea='" + subjectArea + '\'' +
                ", lineManagerUserId='" + lineManagerUserId + '\'' +
                '}';
    }
}
