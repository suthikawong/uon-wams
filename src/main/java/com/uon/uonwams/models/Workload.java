/**
 Program: UON WAMS Application
 Filename: Workload.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import com.uon.uonwams.data.ActivityData;
import com.uon.uonwams.data.ActivityTypeData;
import com.uon.uonwams.data.UserData;
import com.uon.uonwams.data.Data;
import dnl.utils.text.table.TextTable;
import org.apache.commons.beanutils.ConversionException;

import java.util.*;

public class Workload {
    private final ActivityData activityData; // declare activityData to access activities data
    private final UserData userData; // declare activityData to access users data
    private final ActivityTypeData activityTypeData; // declare activityTypeData to access activity types data
    private final List<UserWorkloadAllocation> userWorkloadAllocation = new ArrayList<>();

    public List<UserWorkloadAllocation> getUserWorkloadAllocation() {
        return userWorkloadAllocation;
    }

    public Workload(User loginUser) {
        // initialize data in the system
        this.activityData = Data.activityData;
        this.userData = Data.userData;
        this.activityTypeData = Data.activityTypeData;

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
        // check whether responsibleUserId exists in the system
        Optional<User> responsibleUser = Data.userData.getUsers().stream().filter(user -> user.getUserId() == responsibleUserId).findFirst();
        if (responsibleUser.isEmpty()) {
            System.out.println("Selected responsible user not exist");
            return;
        }
        // add activity
        activityData.insertActivity(activityName, type, description, responsibleUserId, responsibleUser.get().getName(), year, duration, noOfInstances);
    }

    // update a specific activity in the system
    public void updateActivity(int activityId, String activityName, String type, String description, int responsibleUserId, String year, int duration, int noOfInstances) {
        // check whether responsibleUserId exists in the system
        Optional<User> responsibleUser = Data.userData.getUsers().stream().filter(user -> user.getUserId() == responsibleUserId).findFirst();
        if (responsibleUser.isEmpty()) {
            System.out.println("Selected responsible user not exist");
            return;
        }
        // update activity
        activityData.updateActivity(activityId, activityName, type, description, responsibleUserId, responsibleUser.get().getName(), year, duration, noOfInstances);
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

    // import activity types from the CSV file
    public void importActivityTypes(String csvPathname) throws Exception {
        // read activity types data from CSV file
        List<ActivityType> newActivityTypes = activityTypeData.readActivityTypesFromCSV(csvPathname);

        // validation
        importActivityTypesValidation(newActivityTypes);

        // insert new activity types data to DAT file
        activityTypeData.insertActivityTypesToDAT(newActivityTypes);
        // recalculate hours of each activity
        for (Activity activity: activityData.getActivities()) {
            activity.calculateWorkload();
            // update hours in the file
            updateActivity(
                activity.getActivityId(),
                activity.getActivityName(),
                activity.getActivityType(),
                activity.getDescription(),
                activity.getResponsibleUserId(),
                activity.getYear(),
                activity.getDuration(),
                activity.getNoOfInstances()
            );
        }
        // recalculate workload allocation of each user
        for (UserWorkloadAllocation userWorkload: this.userWorkloadAllocation) {
            userWorkload.calculateWorkloadAllocation();
        }
    }

    private void importActivityTypesValidation(List<ActivityType> newActivityTypes) throws Exception {
        // check is there a duplicated activity type
        LinkedHashMap<String, Boolean> set = new LinkedHashMap<>();
        for (ActivityType type: newActivityTypes) {
            set.put(type.getName(), true);
        }
        if (set.size() < newActivityTypes.size()) {
            throw new Exception("Import Failed: Duplicate activity type was found in CSV file.\nPlease try again.");
        }

        // check is the remove activity type have been used
        ActivityType removedActivityType = null;
        for (ActivityType type: activityTypeData.getActivityTypes()) {
            boolean isMatch = newActivityTypes.stream().anyMatch(item -> item.getName().equals(type.getName()));
            if (!isMatch) {
                removedActivityType = type;
                break;
            }
        }
        if (removedActivityType != null) {
            ActivityType finalRemovedActivityType = removedActivityType;
            boolean isMatch = Data.activityData.getActivities().stream().anyMatch(item -> item.getActivityType().equals(finalRemovedActivityType.getName()));
            if (isMatch) {
                throw new Exception("Import Failed: Activity type '" + removedActivityType.name + "' has been used in activities.\nPlease change those activities type before try again.");
            }
        }
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
        int columns = 8;
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
            array[i][7] = user.getParcentageOfTotalHoursAllocated() + "%";
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
        int columns = 10 + Data.activityTypeData.getActivityTypes().size();
        Object[][] array = new Object[rows][columns];

        for (int i = 0; i < rows; i++) {
            Activity activity = list.get(i);
            array[i][0] = activity.getActivityId();
            array[i][1] = activity.getActivityType();
            array[i][2] = activity.getActivityName();
            array[i][3] = activity.getDescription();
            array[i][4] = activity.getResponsibleUserId();
            array[i][5] = activity.getResponsibleUser();
            array[i][6] = activity.getYear();
            array[i][7] = activity.getDuration();
            array[i][8] = activity.getNoOfInstances();
            array[i][9] = activity.getHours();
            int count = 10;
            for(String key : activity.getWorkloadHours().keySet()) {
                array[i][count] = activity.getWorkloadHours().get(key);
                count++;
            }
        }
        return array;
    }
}
