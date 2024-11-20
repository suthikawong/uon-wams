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

    public void toWorkloadPage() {
        state = State.WORKLOAD;
    }

    public State getState() {
        return state;
    }
}
