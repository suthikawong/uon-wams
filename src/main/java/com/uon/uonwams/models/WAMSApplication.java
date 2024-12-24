package com.uon.uonwams.models;

import com.uon.uonwams.data.ActivityData;
import com.uon.uonwams.data.UserData;

public class WAMSApplication {
    public static UserData userData;
    public static ActivityData activityData;

    public WAMSApplication() throws Exception {
        userData = new UserData();
        activityData = new ActivityData();
    }

}
