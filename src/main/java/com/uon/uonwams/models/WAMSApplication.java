package com.uon.uonwams.models;

import com.uon.uonwams.data.ActivityData;
import com.uon.uonwams.data.UserData;
import io.github.cdimascio.dotenv.Dotenv;

public class WAMSApplication {
    public static UserData userData;
    public static ActivityData activityData;

    public WAMSApplication() throws Exception {
        Dotenv dotenv = Dotenv.load();
        String userFilePath = dotenv.get("USER_FILE_PATH");
        String activityFilePath = dotenv.get("ACTIVITY_FILE_PATH");
        userData = new UserData(userFilePath);
        activityData = new ActivityData(activityFilePath);
    }

    public WAMSApplication(String userFilePath, String activityFilePath) throws Exception {
        if (userFilePath != null) userData = new UserData(userFilePath);
        if (activityFilePath != null) activityData = new ActivityData(activityFilePath);
    }

}
