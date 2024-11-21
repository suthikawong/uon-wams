package com.uon.uonwams.models;

import com.uon.uonwams.data.ActivityData;

import java.util.ArrayList;
import java.util.List;

public class Workload {
    private final User user;
    private final List<Activity> activities = new ArrayList<>();

    public Workload(User user) {
        this.user = user;
        for (Activity activity: ActivityData.activities) {
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

    public void addActivity() {

    }

    public void updateActivity() {

    }

    public void deleteActivity() {

    }
}
