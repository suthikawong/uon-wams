/**
 Program: UON WAMS Application
 Filename: MenuController.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class MenuController {
    AppController appController;

    @FXML
    private MenuItem myWorkloadMenuItem;

    @FXML
    private MenuItem profileMenuItem;

    @FXML
    private MenuItem workloadMenuItem;

    @FXML
    private MenuItem umMenuItem;

    // display menu according to the user permission
    protected void setMenuAppController(AppController appController) {
        this.appController = appController;
        // if user is admin
        // my-workload and profile menus will be invisible
        if (appController.getLoginUser().getIsAdmin()) {
            myWorkloadMenuItem.setVisible(false);
            profileMenuItem.setVisible(false);
            return;
        }
        // if user is not line manager
        // workload-management and user-management menus will be invisible
        if (!appController.getLoginUser().checkIsLineManager()) {
            workloadMenuItem.setVisible(false);
            umMenuItem.setVisible(false);
        }
    }

    // navigate to the my-workload.fxml
    @FXML
    protected void onClickMyWorkloadMenuItem() throws IOException {
        appController.loadScene("my-workload.fxml");
    }

    // navigate to the profile-view.fxml
    @FXML
    protected void onClickProfileMenuItem() throws IOException {
        appController.loadScene("profile-view.fxml");
    }

    // navigate to the workload.fxml
    @FXML
    protected void onClickWorkloadMenuItem() throws IOException {
        appController.loadScene("workload.fxml");
    }

    // navigate to the user-view.fxml
    @FXML
    protected void onClickUmMenuItem() throws IOException {
        appController.loadScene("user-view.fxml");
    }

    // navigate to the login.fxml
    @FXML
    protected void onClickLogoutMenuItem() throws IOException {
        appController.loadScene("login.fxml");
    }
}
