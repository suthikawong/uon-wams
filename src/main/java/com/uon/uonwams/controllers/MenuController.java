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

    protected void setMenuAppController(AppController appController) {
        this.appController = appController;
        if (appController.getLoginUser().getIsAdmin()) {
            myWorkloadMenuItem.setVisible(false);
            profileMenuItem.setVisible(false);
            return;
        }
        if (!appController.getLoginUser().checkIsLineManager()) {
            workloadMenuItem.setVisible(false);
            umMenuItem.setVisible(false);
        }
    }

    @FXML
    protected void onClickMyWorkloadMenuItem() throws IOException {
        appController.loadScene("my-workload.fxml");
    }

    @FXML
    protected void onClickProfileMenuItem() throws IOException {
        appController.loadScene("profile-view.fxml");
    }

    @FXML
    protected void onClickWorkloadMenuItem() throws IOException {
        appController.loadScene("workload.fxml");
    }

    @FXML
    protected void onClickUmMenuItem() throws IOException {
        appController.loadScene("user-view.fxml");
    }

    @FXML
    protected void onClickLogoutMenuItem() throws IOException {
        appController.loadScene("login.fxml");
    }
}
