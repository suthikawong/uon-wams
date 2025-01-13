/**
 Program: UON WAMS Application
 Filename: WAMSApplicationConsole.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams;

import com.uon.uonwams.config.State;
import com.uon.uonwams.data.Data;
import com.uon.uonwams.models.*;

import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

public class WAMSApplicationConsole {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        State state;
        try {
            new Data();
            state = State.LOGIN;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
            return;
        }

        User loginUser = null;
        User workloadUser = null;

        while (true) {

            //<editor-fold desc="Login Code">
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
                    // set state
                    state = State.HOME;
                }
                System.out.println("---------------------------------");
                continue;
            }
            //</editor-fold>

            //<editor-fold desc="Home Code">
            if (state == State.HOME) {
                System.out.println("Home Page");
                System.out.printf("A=Workload Management" + (loginUser.getLineManagerUserId() == null ? " | B=User Management" : "") + " | Y=Back: ");
                String command = scanner.next();

                if (command.equalsIgnoreCase("A")) {
                    state = State.VIEW_USER_WORKLOAD;
                } else if (command.equalsIgnoreCase("B")) {
                    state = State.VIEW_USERS;
                } else if (command.equalsIgnoreCase("Y")) {
                    state = State.LOGIN;
                }
                System.out.println("---------------------------------");
                continue;
            }
            //</editor-fold>

            //<editor-fold desc="User Management Code">
            if (loginUser.getLineManagerUserId() == null) {
                UserManagement um = new UserManagement(loginUser);

                if (state == State.VIEW_USERS) {
                    System.out.println("User Management Page");
                    um.logUsers();

                    System.out.print("A=Add User | B=Edit User | C=Delete User | Y=Back: ");
                    String command = scanner.next();

                    if (command.equalsIgnoreCase("A")) {
                        state = State.ADD_USER;
                    } else if (command.equalsIgnoreCase("B")) {
                        state = State.EDIT_USER;
                    } else if (command.equalsIgnoreCase("C")) {
                        state = State.DELETE_USER;
                    } else if (command.equalsIgnoreCase("Y")) {
                        state = State.HOME;
                    }
                    System.out.println("---------------------------------");
                    continue;
                } else if (state == State.ADD_USER) {
                    System.out.println("\nAdd User\n");
                    System.out.print("User ID: ");

                    int userId = scanner.nextInt();
                    // check is user exist
                    Optional<User> user = um.getUserById(userId);
                    if (user.isPresent() || userId == loginUser.getUserId()) {
                        System.out.println("User ID already exists");
                        state = State.VIEW_USERS;
                        continue;
                    }

                    HashMap<String, Object> values = scanUserValues(scanner);

                    try {
                        // add user
                        um.addUser(
                                userId,
                                (String) values.get("name"),
                                (String) values.get("email"),
                                (double) values.get("fteRatio"),
                                (String) values.get("subjectArea"),
                                (Integer) values.get("lineManagerUserId")
                        );
                        System.out.println("Successfully added user");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    // set state
                    state = State.VIEW_USERS;
                    System.out.println("---------------------------------");
                } else if (state == State.EDIT_USER) {
                    System.out.println("\nEdit User\n");
                    System.out.print("Edit User ID: ");

                    int userId = scanner.nextInt();
                    // check is user exist
                    Optional<User> user = um.getUserById(userId);
                    if (user.isEmpty()) {
                        System.out.println("User ID not found or you don't have permission, please select options again");
                        state = State.VIEW_USERS;
                        continue;
                    }

                    HashMap<String, Object> values = scanUserValues(scanner);

                    // update user
                    um.updateUser(
                            userId,
                            (String) values.get("name"),
                            (String) values.get("email"),
                            (double) values.get("fteRatio"),
                            (String) values.get("subjectArea"),
                            (Integer) values.get("lineManagerUserId")
                    );
                    System.out.println("Successfully updated user");

                    // set state
                    state = State.VIEW_USERS;
                    System.out.println("---------------------------------");
                } else if (state == State.DELETE_USER) {
                    System.out.println("\nDelete User\n");
                    System.out.print("Delete User ID: ");

                    int userId = scanner.nextInt();
                    // check is user exist
                    Optional<User> user = um.getUserById(userId);
                    if (user.isEmpty()) {
                        System.out.println("User ID not found or you don't have permission, please select options again");
                        state = State.VIEW_USERS;
                        continue;
                    }

                    um.deleteUser(userId);
                    System.out.println("Successfully deleted user");

                    // set state
                    state = State.VIEW_USERS;
                    System.out.println("---------------------------------");
                }
            }
            //</editor-fold>

            //<editor-fold desc="Workload Code">

            Workload workload = new Workload(loginUser);

            if (state == State.VIEW_USER_WORKLOAD) {
                // log all activities of that user
                System.out.println("Select User Workload");
                workload.logWorkloadUsers();

                System.out.print("Press key to continue or press Y to go back: ");
                String command = scanner.next();

                if (command.equalsIgnoreCase("Y")) {
                    state = State.HOME;
                    continue;
                }

                System.out.print("User ID: ");
                int workloadUserId = scanner.nextInt();

                Optional<UserWorkloadAllocation> user = workload.getWorkloadUserByUserId(workloadUserId);
                if (user.isEmpty()) {
                    System.out.println("Selected User ID is not exist");
                    continue;
                }
                workloadUser = user.get();

                state = State.VIEW_WORKLOAD;
                System.out.println("---------------------------------");
            } else if (state == State.VIEW_WORKLOAD && workloadUser != null) {
                // log all activities of that user
                System.out.println("\nWorkload of: " + workloadUser.getName());
                workload.logActivities(workloadUser.getUserId());

                // set state
                System.out.print("A=Add Activity | B=Edit Activity | C=Delete Activity | Y=Back: ");
                String command = scanner.next();

                if (command.equalsIgnoreCase("A")) {
                    state = State.ADD_ACTIVITY;
                } else if (command.equalsIgnoreCase("B")) {
                    state = State.EDIT_ACTIVITY;
                } else if (command.equalsIgnoreCase("C")) {
                    state = State.DELETE_ACTIVITY;
                } else if (command.equalsIgnoreCase("Y")) {
                    state = State.VIEW_USER_WORKLOAD;
                }
                System.out.println("---------------------------------");
            } else if (state == State.ADD_ACTIVITY) {
                System.out.println("\nAdd Activity\n");

                HashMap<String, Object> values = scanActivityValues(scanner);

                try {
                    // add activity
                    workload.addActivity(
                            (String) values.get("activityName"),
                            (String) values.get("activityType"),
                            (String) values.get("description"),
                            (int) values.get("responsibleUserId"),
                            (String) values.get("year"),
                            (int)values.get("duration"),
                            (int)values.get("noOfInstances")
                    );
                    System.out.println("Successfully added activity");
                } catch (Exception e) {
                    System.out.println("Cannot create activity: " + e.getMessage());
                }

                // set state
                state = State.VIEW_WORKLOAD;
                System.out.println("---------------------------------");
            } else if (state == State.EDIT_ACTIVITY) {
                System.out.println("\nEdit Activity\n");
                System.out.print("Edit Activity ID: ");

                int activityId = scanner.nextInt();
                // check is activity exist
                Optional<Activity> activity = workload.getActivityById(activityId);
                if (activity.isEmpty()) {
                    System.out.println("Not found this activity ID, please select options again");
                    state = State.VIEW_WORKLOAD;
                    continue;
                }

                HashMap<String, Object> values = scanActivityValues(scanner);

                try {
                    // update activity
                    workload.updateActivity(
                            activityId,
                            (String) values.get("activityName"),
                            (String) values.get("activityType"),
                            (String) values.get("description"),
                            (int) values.get("responsibleUserId"),
                            (String) values.get("year"),
                            (int)values.get("duration"),
                            (int)values.get("noOfInstances")
                    );
                    System.out.println("Successfully updated activity");
                } catch (Exception e) {
                    System.out.println("Cannot update activity: " + e.getMessage());
                }

                // set state
                state = State.VIEW_WORKLOAD;
                System.out.println("---------------------------------");
            } else if (state == State.DELETE_ACTIVITY) {
                System.out.println("\nDelete Activity\n");
                System.out.print("Delete Activity ID: ");

                int activityId = scanner.nextInt();
                // check is activity exist
                Optional<Activity> activity = workload.getActivityById(activityId);
                if (activity.isEmpty()) {
                    System.out.println("Not found this activity ID, please select options again");
                    state = State.VIEW_WORKLOAD;
                    continue;
                }

                workload.deleteActivity(activityId);
                System.out.println("Successfully deleted activity");

                // set state
                state = State.VIEW_WORKLOAD;
                System.out.println("---------------------------------");
            }
            //</editor-fold>
        }
    }

    public static HashMap<String, Object> scanActivityValues(Scanner scanner) {
        HashMap<String, Object> values = new HashMap<>();
        scanner.nextLine(); // consume \n that the previous nextInt not consume

        // get new values
        System.out.print("Activity Name: ");
        String activityName = scanner.nextLine();
        values.put("activityName", activityName);

        System.out.print("Activity Type: ");
        String activityType = scanner.nextLine();
        values.put("activityType", activityType);

        System.out.print("Description: ");
        String description = scanner.nextLine();
        values.put("description", description);

        System.out.print("Responsible User ID: ");
        int responsibleUserId = scanner.nextInt();
        values.put("responsibleUserId", responsibleUserId);
        scanner.nextLine();

        System.out.print("Year: ");
        String year = scanner.nextLine();
        values.put("year", year);

        System.out.print("Activity Duration: ");
        int duration = scanner.nextInt();
        values.put("duration", duration);

        System.out.print("No. of Instances: ");
        int noOfInstances = scanner.nextInt();
        values.put("noOfInstances", noOfInstances);

        return values;
    }

    public static HashMap<String, Object> scanUserValues(Scanner scanner) {
        HashMap<String, Object> values = new HashMap<>();
        scanner.nextLine(); // consume \n that the previous nextInt not consume

        // get new values
        System.out.print("Name: ");
        String name = scanner.nextLine();
        values.put("name", name);

        System.out.print("Email: ");
        String email = scanner.nextLine();
        values.put("email", email);

        System.out.print("FTE Ratio: ");
        double fteRatio = scanner.nextDouble();
        values.put("fteRatio", fteRatio);
        scanner.nextLine();

        System.out.print("Subject Area: ");
        String subjectArea = scanner.nextLine();
        values.put("subjectArea", subjectArea);

        System.out.print("Is Line Manager (true/false): ");
        boolean isLineManager = scanner.nextBoolean();

        if (!isLineManager) {
            System.out.print("Line Manager User ID: ");
            int lineManagerUserId = scanner.nextInt();
            values.put("lineManagerUserId", lineManagerUserId);
            scanner.nextLine();
        } else {
            values.put("lineManagerUserId", null);
        }

        return values;
    }
}
