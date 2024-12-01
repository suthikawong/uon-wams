package com.uon.uonwams;

import com.uon.uonwams.config.State;
import com.uon.uonwams.models.*;

import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

public class WAMSApplicationConsole {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WAMSApplication app;
        try {
            app = new WAMSApplication();
        } catch (Exception e) {
            System.exit(0);
            return;
        }

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
                loginUser = new User().login(userId, password);

                if (loginUser == null) {
                    System.out.println("Incorrect user id or password");
                } else {
                    // control flow
                    app.toHomePage();
                }
                System.out.println("---------------------------------");
                continue;
            }

            if (state == State.HOME) {
                System.out.println("Home Page");
                System.out.print("A=Workload | B=Profile | Y=Back: ");
                String command = scanner.next();

                if (command.equalsIgnoreCase("A")) {
                    app.toViewWorkloadPage();
                } else if (command.equalsIgnoreCase("B")) {
                    app.toProfilePage();
                } else if (command.equalsIgnoreCase("Y")) {
                    app.toLoginPage();
                }
                System.out.println("---------------------------------");
                continue;
            }

            Profile profile = new Profile(loginUser);

            if (state == State.PROFILE) {
                System.out.println("Profile Page");
                System.out.print("A=Change password | Y=Back: ");
                String command = scanner.next();

                if (command.equalsIgnoreCase("A")) {
                    app.toChangePasswordPage();
                } else if (command.equalsIgnoreCase("Y")) {
                    app.toHomePage();
                }
                System.out.println("---------------------------------");
                continue;
            } else if (state == State.CHANGE_PASSWORD) {
                System.out.println("Change Password");
                System.out.print("New password: ");
                String password = scanner.next();
                System.out.print("Confirm password: ");
                String confirmPassword = scanner.next();
                if (password.equals(confirmPassword)) {
                    loginUser = profile.changePassword(password);
                    System.out.println(loginUser.getPassword());
                    System.out.println("Successfully change password");
                } else {
                    System.out.println("Password not matched, please try again.");
                }
                app.toProfilePage();
                System.out.println("---------------------------------");
                continue;
            }

            Workload workload = new Workload(loginUser);

            if (state == State.VIEW_WORKLOAD) {
                // log all activities of that user
                System.out.println("\nWorkload of: " + loginUser.getName());
                workload.logActivities();

                // control flow
                System.out.print("A=Add Activity | B=Edit Activity | C=Delete Activity | Y=Back: ");
                String command = scanner.next();

                if (command.equalsIgnoreCase("A")) {
                    app.toAddActivityPage();
                } else if (command.equalsIgnoreCase("B")) {
                    app.toEditActivityPage();
                } else if (command.equalsIgnoreCase("C")) {
                    app.toDeleteActivityPage();
                } else if (command.equalsIgnoreCase("Y")) {
                    app.toHomePage();
                }
                System.out.println("---------------------------------");
            } else if (state == State.ADD_ACTIVITY) {
                System.out.println("\nAdd Activity\n");

                HashMap<String, Object> values = scanActivityValues(scanner);

                // add activity
                workload.addActivity(
                        (String) values.get("activityName"),
                        (String) values.get("type"),
                        (String) values.get("description"),
                        (int) values.get("responsibleUserId"),
                        (String) values.get("responsibleUser"),
                        (String) values.get("year"),
                        (int)values.get("duration"),
                        (int)values.get("weekNo"),
                        (int)values.get("hours"),
                        (int)values.get("ATSR"),
                        (int)values.get("TS"),
                        (int)values.get("TLR"),
                        (int)values.get("SA"),
                        (int)values.get("other")
                );
                System.out.println("Successfully added activity");

                // control flow
                app.toViewWorkloadPage();
                System.out.println("---------------------------------");
            } else if (state == State.EDIT_ACTIVITY) {
                System.out.println("\nEdit Activity\n");
                System.out.print("Edit Activity ID: ");

                int activityId = scanner.nextInt();
                // check is activity exist
                Optional<Activity> activity = workload.getActivityById(activityId);
                if (activity.isEmpty()) {
                    System.out.println("Not found this activity ID, please select options again");
                    app.toViewWorkloadPage();
                    continue;
                }

                HashMap<String, Object> values = scanActivityValues(scanner);

                // update activity
                workload.updateActivity(
                        activityId,
                        (String) values.get("activityName"),
                        (String) values.get("type"),
                        (String) values.get("description"),
                        (int) values.get("responsibleUserId"),
                        (String) values.get("responsibleUser"),
                        (String) values.get("year"),
                        (int)values.get("duration"),
                        (int)values.get("weekNo"),
                        (int)values.get("hours"),
                        (int)values.get("ATSR"),
                        (int)values.get("TS"),
                        (int)values.get("TLR"),
                        (int)values.get("SA"),
                        (int)values.get("other")
                );
                System.out.println("Successfully updated activity");

                // control flow
                app.toViewWorkloadPage();
                System.out.println("---------------------------------");
            } else if (state == State.DELETE_ACTIVITY) {
                System.out.println("\nDelete Activity\n");
                System.out.print("Delete Activity ID: ");

                int activityId = scanner.nextInt();
                // check is activity exist
                Optional<Activity> activity = workload.getActivityById(activityId);
                if (activity.isEmpty()) {
                    System.out.println("Not found this activity ID, please select options again");
                    app.toViewWorkloadPage();
                    continue;
                }

                workload.deleteActivity(activityId);
                System.out.println("Successfully deleted activity");

                // control flow
                app.toViewWorkloadPage();
                System.out.println("---------------------------------");
            }
        }
    }

    public static HashMap<String, Object> scanActivityValues(Scanner scanner) {
        HashMap<String, Object> values = new HashMap<>();
        scanner.nextLine(); // consume \n that the previous nextInt not consume

        // get new values
        System.out.print("Activity Name: ");
        String activityName = scanner.nextLine();
        values.put("activityName", activityName);

        System.out.print("Type: ");
        String type = scanner.nextLine();
        values.put("type", type);

        System.out.print("Description: ");
        String description = scanner.nextLine();
        values.put("description", description);

        // TODO: Display code section depending on user role
        // Role - User: use responsibleUserId, responsibleUser from loginUser
        // Role - Admin: accept responsibleUserId, responsibleUser from console
        System.out.print("Responsible User ID: ");
        int responsibleUserId = scanner.nextInt();
        values.put("responsibleUserId", responsibleUserId);
        scanner.nextLine();

        System.out.print("Responsible User: ");
        String responsibleUser = scanner.nextLine();
        values.put("responsibleUser", responsibleUser);
        // TODO;

        System.out.print("Year: ");
        String year = scanner.nextLine();
        values.put("year", year);

        System.out.print("Duration: ");
        int duration = scanner.nextInt();
        values.put("duration", duration);

        System.out.print("Week No: ");
        int weekNo = scanner.nextInt();
        values.put("weekNo", weekNo);

        System.out.print("Hours: ");
        int hours = scanner.nextInt();
        values.put("hours", hours);

        System.out.print("ATSR: ");
        int ATSR = scanner.nextInt();
        values.put("ATSR", ATSR);

        System.out.print("TS: ");
        int TS = scanner.nextInt();
        values.put("TS", TS);

        System.out.print("TLR: ");
        int TLR = scanner.nextInt();
        values.put("TLR", TLR);

        System.out.print("SA: ");
        int SA = scanner.nextInt();
        values.put("SA", SA);

        System.out.print("Other: ");
        int other = scanner.nextInt();
        values.put("other", other);

        return values;
    }
}
