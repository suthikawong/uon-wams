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
    private final User user;
    private final List<Activity> activities = new ArrayList<>();
    private ActivityData activityData;

    public Workload(User user) {
        this.user = user;
        this.activityData = WAMSApplication.activityData;
        List<Activity> allActivities = activityData.getActivities();

        for (Activity activity: allActivities) {
            if (user.getUserId() == activity.getResponsibleUserId()) {
                this.activities.add(activity);
            }
        }
    }

    public User getUser() {
        return user;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public Optional<Activity> getActivityById(int activityId) {
        return activities.stream()
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

    private ActivityType convertStringToActivityType(String type) {
        if (type.toUpperCase().equals(ActivityType.ATSR.label)) {
            return ActivityType.ATSR;
        } else if (type.toUpperCase().equals(ActivityType.TLR.label)) {
            return ActivityType.TLR;
        } else if (type.toUpperCase().equals(ActivityType.SA.label)) {
            return ActivityType.SA;
        } else if (type.toUpperCase().equals(ActivityType.OTHER.label)) {
            return ActivityType.OTHER;
        } else {
            throw new ConversionException("Invalid activity type");
        }
    }

    public void logActivities() {
        List<String> displayColumns = this.activityData.getAttributes();
        TextTable tt = new TextTable(displayColumns.toArray(new String[0]),convertListToArray(this.activities));
        tt.printTable();
        System.out.println("______________________________________________________________________________________________________________________________________________________________________");
        System.out.println();
    }

    public static Object[][] convertListToArray(List<Activity> list) {
        int rows = list.size();
        int columns = 15;
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
