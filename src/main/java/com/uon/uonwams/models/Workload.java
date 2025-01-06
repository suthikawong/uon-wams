/**
 Program: UON WAMS Application
 Filename: Workload.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import com.uon.uonwams.config.ActivityType;
import com.uon.uonwams.data.ActivityData;
import com.uon.uonwams.data.UserData;
import com.uon.uonwams.data.Data;
import dnl.utils.text.table.TextTable;
import org.apache.commons.beanutils.ConversionException;

import java.util.*;

public class Workload {
    private final ActivityData activityData; // declare activityData to access activities data
    private final UserData userData; // declare activityData to access users data
    private final List<UserWorkloadAllocation> userWorkloadAllocation = new ArrayList<>();

    public List<UserWorkloadAllocation> getUserWorkloadAllocation() {
        return userWorkloadAllocation;
    }

    public Workload(User loginUser) {
        // initialize data in the system
        this.activityData = Data.activityData;
        this.userData = Data.userData;

        // if logged-in user is admin, they have permission to see all users in the system
        if (loginUser.getIsAdmin()) {
            for (User user: userData.getUsers()) {
                this.userWorkloadAllocation.add(new UserWorkloadAllocation(user));
            }
            return;
        }
        // if logged-in user is line manager, they have permission to see their subordinates workload allocation
        for (User user: userData.getUsers()) {
            if (user.getUserId() == loginUser.getUserId() || (user.getLineManagerUserId() != null && user.getLineManagerUserId() == loginUser.getUserId())) {
                this.userWorkloadAllocation.add(new UserWorkloadAllocation(user));
            }
        }
    }

    // get workload allocation of the user by userId
    public Optional<UserWorkloadAllocation> getWorkloadUserByUserId(int userId) {
        return this.userWorkloadAllocation.stream().filter(user -> user.getUserId() == userId).findFirst();
    }

    // get activities by userId
    public List<Activity> getActivitiesByUserId(int userId) {
        List<Activity> activities = new ArrayList<>();
        for (Activity activity: activityData.getActivities()) {
            if (userId == activity.getResponsibleUserId()) {
                activities.add(activity);
            }
        }
        return activities;
    }

    // get activity by activityId
    public Optional<Activity> getActivityById(int activityId) {
        return activityData.getActivities().stream()
                .filter(activity -> activity.getActivityId() == activityId)
                .findFirst();
    }

    // add a new activity to the system
    public void addActivity(String activityName, String type, String description, int responsibleUserId, String year, int duration, int noOfInstances) {
        // convert activity type from String data to ActivityType enum
        ActivityType activityType = convertStringToActivityType(type);
        // check whether responsibleUserId exists in the system
        Optional<User> responsibleUser = Data.userData.getUsers().stream().filter(user -> user.getUserId() == responsibleUserId).findFirst();
        if (responsibleUser.isEmpty()) {
            System.out.println("Selected responsible user not exist");
            return;
        }
        // add activity
        activityData.insertActivity(activityName, activityType, description, responsibleUserId, responsibleUser.get().getName(), year, duration, noOfInstances);
    }

    // update a specific activity in the system
    public void updateActivity(int activityId, String activityName, String type, String description, int responsibleUserId, String year, int duration, int noOfInstances) {
        // convert activity type from String data to ActivityType enum
        ActivityType activityType = convertStringToActivityType(type);
        // check whether responsibleUserId exists in the system
        Optional<User> responsibleUser = Data.userData.getUsers().stream().filter(user -> user.getUserId() == responsibleUserId).findFirst();
        if (responsibleUser.isEmpty()) {
            System.out.println("Selected responsible user not exist");
            return;
        }
        // update activity
        activityData.updateActivity(activityId, activityName, activityType, description, responsibleUserId, responsibleUser.get().getName(), year, duration, noOfInstances);
    }

    public void deleteActivity(int activityId) {
        // delete activity
        activityData.deleteActivity(activityId);
    }

    // import activities from the CSV file
    public void importActivities(String csvPathname) throws Exception {
        CSVFile csvFile = new CSVFile(csvPathname);
        // read data in CSV file
        csvFile.readRecords();
        List<LinkedHashMap<String, String>> importedData = csvFile.getData();
        // remove first record which is the header
        importedData.removeFirst();
        // iterate through CSV data and check whether responsibleUserId in each row exists in the system
        // except for the line manager that responsibleUserId is null
        for (LinkedHashMap<String, String> record: importedData) {
            int responsibleUserId = Integer.parseInt(record.get("responsibleUserId"));
            Optional<User> responsibleUser = userData.getUsers().stream().filter(user -> user.getUserId() == responsibleUserId).findFirst();
            if (responsibleUser.isEmpty()) {
                throw new Exception("User ID not exist");
            }
            record.put("responsibleUser", responsibleUser.get().getName());
        }
        // add multiple activities
        activityData.insertActivities(importedData);
    }

    // search workload allocation of user by their id, name, and subject area
    public List<UserWorkloadAllocation> searchWorkloadAllocationUser(Integer userId, String userName, String subjectArea) {
        List<UserWorkloadAllocation> list = this.getUserWorkloadAllocation();
        if (subjectArea != null && !subjectArea.equals("All")) {
            list = list.stream().filter(user -> user.getSubjectArea().equals(subjectArea)).toList();
        }
        if (userId != null) {
            list = list.stream().filter(user -> user.getUserId() == userId).toList();
        } else if (userName != null && !userName.isBlank()) {
            list = list.stream().filter(user -> user.getName().toLowerCase().contains(userName)).toList();
        }
        return list;
    }

    // convert string type value to ActivityType enum
    private ActivityType convertStringToActivityType(String activityType) {
        if (activityType.equalsIgnoreCase(ActivityType.ATSR.label)) {
            return ActivityType.ATSR;
        } else if (activityType.equalsIgnoreCase(ActivityType.TLR.label)) {
            return ActivityType.TLR;
        } else if (activityType.equalsIgnoreCase(ActivityType.SA.label)) {
            return ActivityType.SA;
        } else if (activityType.equalsIgnoreCase(ActivityType.OTHER.label)) {
            return ActivityType.OTHER;
        } else {
            throw new ConversionException("Invalid activity type");
        }
    }

    // display workload allocations in table format (console application)
    public void logWorkloadUsers() {
        List<String> displayColumns = Arrays.asList("User ID", "Name", "Email", "FTE Ratio", "Subject Area", "Total Hours", "FTE Hours", "Total ATSR + TS", "Parcentage of ATSR allocated", "Parcentage of Total Hours Allocated", "FTE ATSR Hours");
        List<UserWorkloadAllocation> data = this.userWorkloadAllocation;
        TextTable tt = new TextTable(displayColumns.toArray(new String[0]),convertWorkloadUserListToArray(data));
        tt.printTable();
        System.out.println();
    }

    public static Object[][] convertWorkloadUserListToArray(List<UserWorkloadAllocation> list) {
        int rows = list.size();
        int columns = 11;
        Object[][] array = new Object[rows][columns];

        for (int i = 0; i < rows; i++) {
            UserWorkloadAllocation user = list.get(i);
            array[i][0] = user.getUserId();
            array[i][1] = user.getName();
            array[i][2] = user.getEmail();
            array[i][3] = user.getFteRatio();
            array[i][4] = user.getSubjectArea();
            array[i][5] = user.getTotalHours();
            array[i][6] = user.getFteHours();
            array[i][7] = user.getTotalAtsrTs();
            array[i][8] = user.getPercentageOfAtsrAllocated() + "%";
            array[i][9] = user.getParcentageOfTotalHoursAllocated() + "%";
            array[i][10] = user.getFteAtsrHours();
        }
        return array;
    }

    // display activities in table format (console application)
    public void logActivities(int userId) {
        List<String> displayColumns = Arrays.asList("Activity ID", "Activity Type", "Activity Name", "Description", "Responsible User ID", "Responsible User", "Year", "Duration", "No of Instances", "Hours", "ATSR", "TS", "TLR", "SA", "Other");
        List<Activity> data = getActivitiesByUserId(userId);
        TextTable tt = new TextTable(displayColumns.toArray(new String[0]),convertWorkloadListToArray(data));
        tt.printTable();
        System.out.println();
    }

    public static Object[][] convertWorkloadListToArray(List<Activity> list) {
        int rows = list.size();
        int columns = 15;
        Object[][] array = new Object[rows][columns];

        for (int i = 0; i < rows; i++) {
            Activity activity = list.get(i);
            array[i][0] = activity.getActivityId();
            array[i][1] = activity.getActivityType().label;
            array[i][2] = activity.getActivityName();
            array[i][3] = activity.getDescription();
            array[i][4] = activity.getResponsibleUserId();
            array[i][5] = activity.getResponsibleUser();
            array[i][6] = activity.getYear();
            array[i][7] = activity.getDuration();
            array[i][8] = activity.getNoOfInstances();
            array[i][9] = activity.getHours();
            array[i][10] = activity.getATSR();
            array[i][11] = activity.getTS();
            array[i][12] = activity.getTLR();
            array[i][13] = activity.getSA();
            array[i][14] = activity.getOther();
        }
        return array;
    }
}
