package com.uon.uonwams.controllers;

import com.uon.uonwams.config.State;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class MenuController {
    AppController appController;

    @FXML
    private MenuItem workloadMenuItem;

    @FXML
    private MenuItem umMenuItem;

    protected void setMenuAppController(AppController appController) {
        this.appController = appController;
        if (!appController.getLoginUser().checkIsLineManager()) {
            workloadMenuItem.setVisible(false);
            umMenuItem.setVisible(false);
        }
    }

    @FXML
    protected void onClickMyWorkloadMenuItem() throws IOException {
        appController.setState(State.MY_WORKLOAD);
        appController.loadScene("my-workload.fxml");
    }

    @FXML
    protected void onClickProfileMenuItem() throws IOException {
        appController.setState(State.VIEW_PROFILE);
        appController.loadScene("profile-view.fxml");
    }

    @FXML
    protected void onClickWorkloadMenuItem() throws IOException {
        appController.setState(State.VIEW_USER_WORKLOAD);
        appController.loadScene("workload.fxml");
    }

    @FXML
    protected void onClickUmMenuItem() throws IOException {
        appController.setState(State.VIEW_USERS);
        appController.loadScene("user-view.fxml");
    }

    @FXML
    protected void onClickLogoutMenuItem() throws IOException {
        appController.setState(State.LOGIN);
        appController.loadScene("login.fxml");
    }
}
