package com.uon.uonwams.models;

import com.uon.uonwams.config.ActivityType;
import com.uon.uonwams.data.ActivityData;
import com.uon.uonwams.data.UserData;
import dnl.utils.text.table.TextTable;
import org.apache.commons.beanutils.ConversionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Workload {
    private final User loginUser;
//    private final List<Activity> activities = new ArrayList<>();
    private ActivityData activityData;
    private UserData userData;
    private List<User> workloadMembers = new ArrayList<>();

    public Workload(User loginUser) {
        this.loginUser = loginUser;
        this.activityData = WAMSApplication.activityData;
        this.userData = WAMSApplication.userData;
        for (User user: userData.getUsers()) {
            if (user.getUserId() == loginUser.getUserId() || (user.getLineManagerUserId() != null && user.getLineManagerUserId() == loginUser.getUserId())) {
                this.workloadMembers.add(user);
            }
        }
    }

    public Optional<User> getWorkloadMemberByUserId(int userId) {
        return this.workloadMembers.stream().filter(user -> user.getUserId() == userId).findFirst();
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

    public Activity addActivity(String activityName, String type, String description, int responsibleUserId, String year, int duration, int weekNo) {
        try {
            ActivityType activityType = convertStringToActivityType(type);
            Optional<User> responsibleUser = WAMSApplication.userData.getUsers().stream().filter(user -> user.getUserId() == responsibleUserId).findFirst();
            if (responsibleUser.isEmpty()) {
                System.out.println("Selected responsible user not exist");
                return null;
            }
            return activityData.insertActivity(activityName, activityType, description, responsibleUserId, responsibleUser.get().getName(), year, duration, weekNo);
        } catch (Exception e) {
            System.out.println("Cannot add activity: " + e.getMessage());
        }
        return null;
    }

    public Activity updateActivity(int activityId, String activityName, String type, String description, int responsibleUserId, String year, int duration, int weekNo) {
        try {
            ActivityType activityType = convertStringToActivityType(type);
            Optional<User> responsibleUser = WAMSApplication.userData.getUsers().stream().filter(user -> user.getUserId() == responsibleUserId).findFirst();
            if (responsibleUser.isEmpty()) {
                System.out.println("Selected responsible user not exist");
                return null;
            }
            return activityData.updateActivity(activityId, activityName, activityType, description, responsibleUserId, responsibleUser.get().getName(), year, duration, weekNo);
        } catch (Exception e) {
            System.out.println("Cannot update activity: " + e.getMessage());
        }
        return null;
    }

    public Integer deleteActivity(int activityId) {
        try {
            return activityData.deleteActivity(activityId);
        } catch (Exception e) {
            System.out.println("Cannot delete activity: " + e.getMessage());
        }
        return null;
    }

    private ActivityType convertStringToActivityType(String activityType) {
        System.out.println(activityType.toUpperCase());
        System.out.println(ActivityType.OTHER.label);
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
        List<String> displayColumns = new ArrayList<>(this.userData.getAttributes());
        displayColumns = displayColumns.stream().filter(column -> !column.equals("password") && !column.equals("lineManagerUserId")).toList();
        TextTable tt = new TextTable(displayColumns.toArray(new String[0]),convertWorkloadUserListToArray(this.workloadMembers));
        tt.printTable();
        System.out.println("_______________________________________________________________________________");
        System.out.println();
    }

    public static Object[][] convertWorkloadUserListToArray(List<User> list) {
        int rows = list.size();
        int columns = 5;
        Object[][] array = new Object[rows][columns];

        for (int i = 0; i < rows; i++) {
            User user = list.get(i);
            array[i][0] = user.getUserId();
            array[i][1] = user.getName();
            array[i][2] = user.getEmail();
            array[i][3] = user.getContractType().label;
            array[i][4] = user.getSubjectArea();
        }
        return array;
    }

    public void logActivities(int userId) {
        List<String> displayColumns = this.activityData.getAttributes();
        TextTable tt = new TextTable(displayColumns.toArray(new String[0]),convertWorkloadListToArray(getActivitiesByUserId(userId)));
        tt.printTable();
        System.out.println("______________________________________________________________________________________________________________________________________________________________________");
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
            array[i][8] = activity.getWeekNo();
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
