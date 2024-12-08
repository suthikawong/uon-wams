package com.uon.uonwams.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MenuController {
    AppController appController;

    protected void setMenuAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    protected void onClickMyWorkloadMenuItem(ActionEvent event) throws IOException {
        appController.loadScene("my-workload.fxml");
    }

    @FXML
    protected void onClickProfileMenuItem(ActionEvent event) throws IOException {
        appController.loadScene("profile.fxml");
    }

    @FXML
    protected void onClickWorkloadMenuItem(ActionEvent event) throws IOException {
        appController.loadScene("workload.fxml");
    }

    @FXML
    protected void onClickUmMenuItem(ActionEvent event) throws IOException {
        appController.loadScene("um.fxml");
    }
}
