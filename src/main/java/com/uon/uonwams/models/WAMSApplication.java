package com.uon.uonwams.models;

import com.uon.uonwams.config.State;
import com.uon.uonwams.data.ActivityData;
import com.uon.uonwams.data.UserData;

public class WAMSApplication {
    public static UserData userData;
    public static ActivityData activityData;
    private State state;

    public WAMSApplication() throws Exception {
        userData = new UserData();
        activityData = new ActivityData();
        toLoginPage();
    }

    public void toLoginPage() {
        state = State.LOGIN;
    }

    public void toHomePage() {
        state = State.HOME;
    }

    public void toProfilePage() {
        state = State.PROFILE;
    }

    public void toChangePasswordPage() {
        state = State.CHANGE_PASSWORD;
    }

    public void toViewWorkloadPage() {
        state = State.VIEW_WORKLOAD;
    }

    public void toAddActivityPage() {
        state = State.ADD_ACTIVITY;
    }

    public void toEditActivityPage() {
        state = State.EDIT_ACTIVITY;
    }

    public void toDeleteActivityPage() {
        state = State.DELETE_ACTIVITY;
    }

    public void toViewUserManagementPage() {
        state = State.VIEW_USERS;
    }

    public void toAddUserPage() {
        state = State.ADD_USER;
    }

    public void toEditUserPage() {
        state = State.EDIT_USER;
    }

    public void toDeleteUserPage() {
        state = State.DELETE_USER;
    }

    public State getState() {
        return state;
    }
}
