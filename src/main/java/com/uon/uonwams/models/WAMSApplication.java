package com.uon.uonwams.models;

import com.uon.uonwams.config.State;

public class WAMSApplication {
    private State state;

    public WAMSApplication() {
        toLoginPage();
    }

    public void toLoginPage() {
        state = State.LOGIN;
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

    public State getState() {
        return state;
    }
}
