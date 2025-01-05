package com.uon.uonwams.models;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import com.uon.uonwams.data.Data;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    // initialize bcrypt
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

    // find all subordinates of this user
    public List<User> getSubordinate() {
        // if this user is admin, return all users in the system
        if (this.isAdmin) {
            return new ArrayList<>(Data.userData.getUsers());
        }
        List<User> subordinateList = new ArrayList<>();
        for (User user: Data.userData.getUsers()) {
            // if this user isn't admin, return users that their line manager is this user
            if (((Integer) this.userId).equals(user.getLineManagerUserId())) {
                subordinateList.add(user);
            }
        }
        return subordinateList;
    }

    // check whether this user is line manager or not
    public boolean checkIsLineManager() {
        for (User user: Data.userData.getUsers()) {
            // if at least one staff have this user as their line manager, return true
            if (((Integer) this.userId).equals(user.getLineManagerUserId())) {
                return true;
            }
        }
        return false;
    }

    // attach user information when userId and password are matched with the one in the system
    public User login(int userId, String password) {
        Dotenv dotenv = Dotenv.load();
        // get value ENABLE_ADMIN_USER from .env file
        // if it is "true", admin account will available
        String enableAdminUser = dotenv.get("ENABLE_ADMIN_USER");

        // if admin user is available
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
            // check whether userId and password are match with ADMIN_USER_ID and ADMIN_PASSWORD
            // if they match, assign admin information
            if (adminUserId == userId && adminPassword.equals(password)) {
                this.userId = userId;
                this.name = "Admin";
                this.isAdmin = true;
                return this;
            }
        }

        for (User user: Data.userData.getUsers()) {
            // check whether userId and password are match with the one in the system
            // if they match, assign the match user information
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

    // hash and update password in the system
    public void changePassword(String password) throws Exception {
        Data.userData.updateUser(userId, name, hashPassword(password), email, fteRatio, subjectArea, lineManagerUserId);
    }

    // generate new password and update it into the system
    public static String resetPassword(User user) throws Exception {
        String password = getRandomPassword();
        Data.userData.updateUser(user.getUserId(), user.getName(), hashPassword(password), user.getEmail(), user.getFteRatio(), user.getSubjectArea(), user.getLineManagerUserId());
        return password;
    }

    // generate 6 digits password
    public static String getRandomPassword() {
        // Ref: https://stackoverflow.com/questions/51322750/generate-6-digit-random-number
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    // hash password
    public static String hashPassword(String password) {
        // Ref: https://davidbertoldi.medium.com/hashing-passwords-in-java-757e787ce71c
        Dotenv dotenv = Dotenv.load();
        String secret = dotenv.get("PASSWORD_SECRET");
        Hash hash = Password.hash(password)
                .addPepper(secret)
                .with(bcrypt);
        return hash.getResult();
    }

    // check are the input passwords match
    private boolean isMatchedPassword(String plainPassword, String storedPassword) {
        Dotenv dotenv = Dotenv.load();
        String secret = dotenv.get("PASSWORD_SECRET");
        return Password.check(plainPassword, storedPassword)
                .addPepper(secret)
                .with(bcrypt);
    }
}
