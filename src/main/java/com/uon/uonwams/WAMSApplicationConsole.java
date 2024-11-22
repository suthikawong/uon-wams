package com.uon.uonwams;

import com.uon.uonwams.config.State;
import com.uon.uonwams.data.ActivityData;
import com.uon.uonwams.data.UserData;
import com.uon.uonwams.models.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WAMSApplicationConsole {

    public static void main(String[] args) throws FileNotFoundException {
        // get all users from csv file
        UserData userData = new UserData();
        System.out.println(userData.users);


        Scanner scanner = new Scanner(System.in);

        WAMSApplication app = new WAMSApplication();

        User loginUser = null;

        while (true) {
            State state = app.getState();

            if (state == State.LOGIN) {
                System.out.println("Please enter user id and password");
                System.out.print("User Id: ");
                int userId = scanner.nextInt();
                System.out.print("Password: ");
                String password = scanner.next();

                // check user id & password
                Authentication auth = new Authentication();
                loginUser = auth.login(userId, password);

                if (loginUser == null) {
                    System.out.println("Incorrect user id or password");
                } else {
                    // control flow
                    app.toViewWorkloadPage();
                }
                System.out.println("---------------------------------");
                continue;
            }

            Workload workload = new Workload(loginUser);

            if (state == State.VIEW_WORKLOAD) {
                // log all activities of that user
                System.out.println("\nWorkload of: " + loginUser.getName());
                workload.logActivities();

                // control flow
                System.out.print("A=Edit Activity, B=Delete Activity, C=Back: ");
                String command = scanner.next();

                if (command.equalsIgnoreCase("A")) {
                    app.toEditActivityPage();
                } else if (command.equalsIgnoreCase("B")) {
                    app.toDeleteActivityPage();
                } else if (command.equalsIgnoreCase("C")) {
                    app.toLoginPage();
                }
                System.out.println("---------------------------------");
            } else if (state == State.EDIT_ACTIVITY) {
                System.out.println("\nEdit Activity");
                System.out.print("Edit Activity ID: ");

                int activityId = scanner.nextInt();
                // find selected activity
                Optional<Activity> activity = workload.getActivityById(activityId);
                if (activity.isEmpty()) {
                    System.out.println("Not found this activity ID, please select options again");
                    app.toViewWorkloadPage();
                    continue;
                }

                // get new values
                System.out.print("Activity Name: ");
                String activityName = scanner.nextLine();
                System.out.print("Type: ");
                String type = scanner.next();
                System.out.print("Description: ");
                String description = scanner.nextLine();
                System.out.print("Responsible User ID: ");
                int responsibleUserId = scanner.nextInt();
                System.out.print("Responsible User: ");
                String responsibleUser = scanner.nextLine();
                System.out.print("Year: ");
                String year = scanner.nextLine();
                System.out.print("Duration: ");
                int duration = scanner.nextInt();
                System.out.print("Week No: ");
                int weekNo = scanner.nextInt();
                System.out.print("Hours: ");
                int hours = scanner.nextInt();
                System.out.print("ATSR: ");
                int ATSR = scanner.nextInt();
                System.out.print("TS: ");
                int TS = scanner.nextInt();
                System.out.print("TLR: ");
                int TLR = scanner.nextInt();
                System.out.print("SA: ");
                int SA = scanner.nextInt();
                System.out.print("Other: ");
                int other = scanner.nextInt();

                // update activity
                workload.updateActivity(activityId, activityName, type, description, responsibleUserId, responsibleUser, year, duration, weekNo, hours, ATSR, TS, TLR, SA, other);
                System.out.print("Successfully updated");

                // control flow
                app.toViewWorkloadPage();
                System.out.println("---------------------------------");
            } else if (state == State.DELETE_ACTIVITY) {
                //
            }
        }
    }
}
