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
            System.out.println(e.getMessage());
            System.exit(0);
            return;
//            throw e;
        }

        User loginUser = null;
        User workloadUser = null;

        while (true) {
            State state = app.getState();

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
                    // control flow
                    app.toHomePage();
                }
                System.out.println("---------------------------------");
                continue;
            }
            //</editor-fold>

            //<editor-fold desc="Home Code">
            if (state == State.HOME) {
                System.out.println("Home Page");
                System.out.printf("A=Workload Management | B=Profile" + (loginUser.getLineManagerUserId() == null ? " | C=User Management" : "") + " | Y=Back: ");
                String command = scanner.next();

                if (command.equalsIgnoreCase("A")) {
                    app.toViewUserWorkloadPage();
                } else if (command.equalsIgnoreCase("B")) {
                    app.toProfilePage();
                } else if (command.equalsIgnoreCase("C")) {
                    app.toViewUserManagementPage();
                } else if (command.equalsIgnoreCase("Y")) {
                    app.toLoginPage();
                }
                System.out.println("---------------------------------");
                continue;
            }
            //</editor-fold>

            //<editor-fold desc="Profile Code">

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
                        app.toAddUserPage();
                    } else if (command.equalsIgnoreCase("B")) {
                        app.toEditUserPage();
                    } else if (command.equalsIgnoreCase("C")) {
                        app.toDeleteUserPage();
                    } else if (command.equalsIgnoreCase("Y")) {
                        app.toHomePage();
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
                        app.toViewUserManagementPage();
                        continue;
                    }

                    HashMap<String, Object> values = scanUserValues(scanner);

                    // add user
                    um.addUser(
                            userId,
                            (String) values.get("name"),
                            (String) values.get("email"),
                            (String) values.get("contractType"),
                            (String) values.get("subjectArea"),
                            (Integer) values.get("lineManagerUserId")
                    );
                    System.out.println("Successfully added user");

                    // control flow
                    app.toViewUserManagementPage();
                    System.out.println("---------------------------------");
                } else if (state == State.EDIT_USER) {
                    System.out.println("\nEdit User\n");
                    System.out.print("Edit User ID: ");

                    int userId = scanner.nextInt();
                    // check is user exist
                    Optional<User> user = um.getUserById(userId);
                    if (user.isEmpty()) {
                        System.out.println("User ID not found or you don't have permission, please select options again");
                        app.toViewUserManagementPage();
                        continue;
                    }

                    HashMap<String, Object> values = scanUserValues(scanner);

                    // update user
                    um.updateUser(
                            userId,
                            (String) values.get("name"),
                            (String) values.get("email"),
                            (String) values.get("contractType"),
                            (String) values.get("subjectArea"),
                            (Integer) values.get("lineManagerUserId")
                    );
                    System.out.println("Successfully updated user");

                    // control flow
                    app.toViewUserManagementPage();
                    System.out.println("---------------------------------");
                } else if (state == State.DELETE_USER) {
                    System.out.println("\nDelete User\n");
                    System.out.print("Delete User ID: ");

                    int userId = scanner.nextInt();
                    // check is user exist
                    Optional<User> user = um.getUserById(userId);
                    if (user.isEmpty()) {
                        System.out.println("User ID not found or you don't have permission, please select options again");
                        app.toViewUserManagementPage();
                        continue;
                    }

                    um.deleteUser(userId);
                    System.out.println("Successfully deleted user");

                    // control flow
                    app.toViewUserManagementPage();
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
                    app.toHomePage();
                    continue;
                }

                System.out.print("User ID: ");
                int workloadUserId = scanner.nextInt();

                Optional<User> user = workload.getWorkloadMemberByUserId(workloadUserId);
                if (user.isEmpty()) {
                    System.out.println("Selected User ID is not exist");
                    continue;
                }
                workloadUser = user.get();

                app.toViewWorkloadPage();
                System.out.println("---------------------------------");
            } else if (state == State.VIEW_WORKLOAD && workloadUser != null) {
                // log all activities of that user
                System.out.println("\nWorkload of: " + workloadUser.getName());
                workload.logActivities(workloadUser.getUserId());

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
                    app.toViewUserWorkloadPage();
                }
                System.out.println("---------------------------------");
            } else if (state == State.ADD_ACTIVITY) {
                System.out.println("\nAdd Activity\n");

                HashMap<String, Object> values = scanActivityValues(scanner);

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
                        (String) values.get("activityType"),
                        (String) values.get("description"),
                        (int) values.get("responsibleUserId"),
                        (String) values.get("year"),
                        (int)values.get("duration"),
                        (int)values.get("noOfInstances")
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

        // TODO: Display code section depending on user role
        // Role - User: use responsibleUserId, responsibleUser from loginUser
        // Role - Admin: accept responsibleUserId, responsibleUser from console
        System.out.print("Responsible User ID: ");
        int responsibleUserId = scanner.nextInt();
        values.put("responsibleUserId", responsibleUserId);
        scanner.nextLine();
        // TODO;

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

        System.out.print("Contract Type: ");
        String contractType = scanner.nextLine();
        values.put("contractType", contractType);

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
