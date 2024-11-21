package com.uon.uonwams;

import com.uon.uonwams.config.State;
import com.uon.uonwams.data.ActivityData;
import com.uon.uonwams.data.UserData;
import com.uon.uonwams.models.Activity;
import com.uon.uonwams.models.Authentication;
import com.uon.uonwams.models.User;
import com.uon.uonwams.models.WAMSApplication;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class WAMSApplicationConsole {

    public static void main(String[] args) throws FileNotFoundException {
        // get all users from csv file
        UserData userData = new UserData();
        List<User> allUsers = userData.getUsers();
        System.out.println(allUsers);

        ActivityData activityData = new ActivityData();
        List<Activity> allActivities = activityData.getActivities();
        System.out.println(allActivities);

        Scanner scanner = new Scanner(System.in);

        WAMSApplication app = new WAMSApplication();

        User loginUser;

        while (true) {
            State state = app.getState();

            if (state == State.LOGIN) {
                System.out.println("Please enter user id and password");
                System.out.print("User Id: ");
                int userId = scanner.nextInt();
                System.out.print("Password: ");
                String password = scanner.next();

                Authentication auth = new Authentication(allUsers);
                loginUser = auth.login(userId, password);
                if (loginUser == null) {
                    System.out.println("Incorrect user id or password");
                } else {
                    app.toWorkloadPage();
                }
                System.out.println("---------------------------------");
            } else if (state == State.WORKLOAD) {
                System.out.println("Workload");

                System.out.print("A=Continue, X=Logout: ");
                String command = scanner.next();

                if (command.equalsIgnoreCase("X")) {
                    app.toLoginPage();
                }
                System.out.println("---------------------------------");
            }
        }
    }
}
