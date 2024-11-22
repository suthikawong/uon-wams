package com.uon.uonwams.models;

import com.uon.uonwams.data.ActivityData;
import dnl.utils.text.table.TextTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Activity> getActivities() {
        return activities;
    }

    public Optional<Activity> getActivityById(int activityId) {
        return activities.stream()
                .filter(activity -> activity.getActivityId() == activityId)
                .findFirst();
    }

    public void addActivity() {

    }

    public Activity updateActivity(int activityId, String activityName, String type, String description, int responsibleUserId, String responsibleUser, String year, int duration, int weekNo, int hours, int ATSR, int TS, int TLR, int SA, int other) {
        return activityData.updateActivity(activityId, activityName, type, description, responsibleUserId, responsibleUser, year, duration, weekNo, hours, ATSR, TS, TLR, SA, other);
    }

    public void deleteActivity() {

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
            array[i][1] = activity.getType();
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
