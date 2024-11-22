package com.uon.uonwams.models;

import com.uon.uonwams.data.ActivityData;
import dnl.utils.text.table.TextTable;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Workload {
    private final User user;
    private final List<Activity> activities = new ArrayList<>();
    private ActivityData activityData;

    public Workload(User user) {
        this.user = user;
        activityData = new ActivityData();
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

    public void logActivities() {
        List<String> displayColumns = new ArrayList<>();
        for (String column: this.activityData.getAttributes()) {
            if (column.equals("activityId") || column.equals("responsibleUserId")) continue;
            displayColumns.add(column);
        }
        TextTable tt = new TextTable(displayColumns.toArray(new String[0]),convertListToArray(this.activities));
        tt.printTable();
        System.out.println("______________________________________________________________________________________________________________________________________________________________________");
        System.out.println();
    }

    public static Object[][] convertListToArray(List<Activity> list) {
        int rows = list.size();
        int columns = 13;
        Object[][] array = new Object[rows][columns];

        for (int i = 0; i < rows; i++) {
            Activity activity = list.get(i);
            array[i][0] = activity.getType();
            array[i][1] = activity.getActivityName();
            array[i][2] = activity.getDescription();
            array[i][3] = activity.getResponsibleUser();
            array[i][4] = activity.getYear();
            array[i][5] = activity.getDuration();
            array[i][6] = activity.getWeekNo();
            array[i][7] = activity.getHours();
            array[i][8] = activity.getATSR();
            array[i][9] = activity.getTS();
            array[i][10] = activity.getTLR();
            array[i][11] = activity.getSA();
            array[i][12] = activity.getOther();
        }
        return array;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void addActivity() {

    }

    public void updateActivity() {

    }

    public void deleteActivity() {

    }
}
