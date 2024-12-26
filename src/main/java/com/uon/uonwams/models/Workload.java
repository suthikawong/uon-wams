package com.uon.uonwams.models;

import com.uon.uonwams.config.ActivityType;
import com.uon.uonwams.data.ActivityData;
import com.uon.uonwams.data.UserData;
import dnl.utils.text.table.TextTable;
import org.apache.commons.beanutils.ConversionException;

import java.util.*;

public class Workload {
    private final ActivityData activityData;
    private final UserData userData;
    private final List<UserWorkloadAllocation> userWorkloadAllocation = new ArrayList<>();

    public List<UserWorkloadAllocation> getUserWorkloadAllocation() {
        return userWorkloadAllocation;
    }

    public Workload(User loginUser) {
        this.activityData = WAMSApplication.activityData;
        this.userData = WAMSApplication.userData;
        if (loginUser.getIsAdmin()) {
            for (User user: userData.getUsers()) {
                this.userWorkloadAllocation.add(new UserWorkloadAllocation(user));
            }
            return;
        }
        for (User user: userData.getUsers()) {
            if (user.getUserId() == loginUser.getUserId() || (user.getLineManagerUserId() != null && user.getLineManagerUserId() == loginUser.getUserId())) {
                this.userWorkloadAllocation.add(new UserWorkloadAllocation(user));
            }
        }
    }

    public Optional<UserWorkloadAllocation> getWorkloadUserByUserId(int userId) {
        return this.userWorkloadAllocation.stream().filter(user -> user.getUserId() == userId).findFirst();
    }

    public List<Activity> getActivitiesByUserId(int userId) {
        List<Activity> activities = new ArrayList<>();
        for (Activity activity: activityData.getActivities()) {
            if (userId == activity.getResponsibleUserId()) {
                activities.add(activity);
            }
        }
        return activities;
    }

    public Optional<Activity> getActivityById(int activityId) {
        return activityData.getActivities().stream()
                .filter(activity -> activity.getActivityId() == activityId)
                .findFirst();
    }

    public void addActivity(String activityName, String type, String description, int responsibleUserId, String year, int duration, int noOfInstances) {
        ActivityType activityType = convertStringToActivityType(type);
        Optional<User> responsibleUser = WAMSApplication.userData.getUsers().stream().filter(user -> user.getUserId() == responsibleUserId).findFirst();
        if (responsibleUser.isEmpty()) {
            System.out.println("Selected responsible user not exist");
            return;
        }
        activityData.insertActivity(activityName, activityType, description, responsibleUserId, responsibleUser.get().getName(), year, duration, noOfInstances);
    }

    public void updateActivity(int activityId, String activityName, String type, String description, int responsibleUserId, String year, int duration, int noOfInstances) {
        ActivityType activityType = convertStringToActivityType(type);
        Optional<User> responsibleUser = WAMSApplication.userData.getUsers().stream().filter(user -> user.getUserId() == responsibleUserId).findFirst();
        if (responsibleUser.isEmpty()) {
            System.out.println("Selected responsible user not exist");
            return;
        }
        activityData.updateActivity(activityId, activityName, activityType, description, responsibleUserId, responsibleUser.get().getName(), year, duration, noOfInstances);
    }

    public void deleteActivity(int activityId) {
        activityData.deleteActivity(activityId);
    }

    public void importActivities(String csvPathname) throws Exception {
        CSVFile csvFile = new CSVFile(csvPathname);
        csvFile.readRecord();
        List<LinkedHashMap<String, String>> importedData = csvFile.getData();
        importedData.removeFirst();
        for (LinkedHashMap<String, String> record: importedData) {
            int responsibleUserId = Integer.parseInt(record.get("responsibleUserId"));
            Optional<User> responsibleUser = userData.getUsers().stream().filter(user -> user.getUserId() == responsibleUserId).findFirst();
            if (responsibleUser.isEmpty()) {
                throw new Exception("User ID not exist");
            }
            record.put("responsibleUser", responsibleUser.get().getName());
        }
        activityData.insertActivities(importedData);
    }

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
