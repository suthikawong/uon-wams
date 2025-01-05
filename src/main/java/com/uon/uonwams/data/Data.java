/**
 Program: UON WAMS Application
 Filename: Data.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.data;

import io.github.cdimascio.dotenv.Dotenv;

public class Data {
    public static UserData userData;
    public static ActivityData activityData;

    // initialize ActivityData and UserData instance from file path in .env file
    public Data() throws Exception {
        Dotenv dotenv = Dotenv.load();
        String userFilePath = dotenv.get("USER_FILE_PATH");
        String activityFilePath = dotenv.get("ACTIVITY_FILE_PATH");
        userData = new UserData(userFilePath);
        activityData = new ActivityData(activityFilePath);
    }

    // initialize ActivityData and UserData instance from input parameters
    public Data(String userFilePath, String activityFilePath) throws Exception {
        if (userFilePath != null) userData = new UserData(userFilePath);
        if (activityFilePath != null) activityData = new ActivityData(activityFilePath);
    }

}
