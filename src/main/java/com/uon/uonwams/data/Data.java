package com.uon.uonwams.data;

import io.github.cdimascio.dotenv.Dotenv;

public class Data {
    public static UserData userData;
    public static ActivityData activityData;

    public Data() throws Exception {
        Dotenv dotenv = Dotenv.load();
        String userFilePath = dotenv.get("USER_FILE_PATH");
        String activityFilePath = dotenv.get("ACTIVITY_FILE_PATH");
        userData = new UserData(userFilePath);
        activityData = new ActivityData(activityFilePath);
    }

    public Data(String userFilePath, String activityFilePath) throws Exception {
        if (userFilePath != null) userData = new UserData(userFilePath);
        if (activityFilePath != null) activityData = new ActivityData(activityFilePath);
    }

}
