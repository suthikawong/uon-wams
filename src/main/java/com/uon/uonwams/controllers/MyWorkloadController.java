package com.uon.uonwams.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MyWorkloadController implements BaseController {
    AppController appController;

    @FXML
    protected void onClickBackButton(ActionEvent event) throws IOException {
        appController.loadScene("login.fxml");
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }
}
