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
    public static ConfigurationData configurationData;

    // initialize ActivityData, UserData, and ConfigurationData instance from file path in .env file
    public Data() throws Exception {
        Dotenv dotenv = Dotenv.load();
        String userFilePath = dotenv.get("USER_FILE_PATH");
        String activityFilePath = dotenv.get("ACTIVITY_FILE_PATH");
        String configurationFilePath = dotenv.get("CONFIGURATION_FILE_PATH");
        userData = new UserData(userFilePath);
        activityData = new ActivityData(activityFilePath);
        configurationData = new ConfigurationData(configurationFilePath);
    }

    // initialize ActivityData UserData, and ActivityTypeData instance from input parameters
    public Data(String userFilePath, String activityFilePath, String configurationFilePath) throws Exception {
        if (userFilePath != null) userData = new UserData(userFilePath);
        if (activityFilePath != null) activityData = new ActivityData(activityFilePath);
        if (configurationFilePath != null) configurationData = new ConfigurationData(configurationFilePath);
    }

}
