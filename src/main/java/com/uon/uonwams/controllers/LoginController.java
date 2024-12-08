package com.uon.uonwams.controllers;

import com.uon.uonwams.config.State;
import com.uon.uonwams.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController implements ControllerInterface {
    private AppController appController;

    @FXML
    private TextField loginUserIdTextField;

    @FXML
    private TextField loginPasswordTextField;

    @FXML
    private Label loginUserIdErrorLabel;

    @FXML
    private Label loginErrorLabel;


    public void setup() {
        loginUserIdErrorLabel.setVisible(false);
        loginErrorLabel.setVisible(false);
    }


    @FXML
    protected void onClickLoginButton(ActionEvent event) throws IOException {
        int userId;
        try {
            userId = Integer.parseInt(loginUserIdTextField.getText());
        } catch(Exception e) {
            loginUserIdErrorLabel.setText("Invalid User ID");
            loginUserIdErrorLabel.setVisible(true);
            return;
        }

        User loginUser = new User().login(userId, loginPasswordTextField.getText());
        if (loginUser == null) {
            loginErrorLabel.setText("Incorrect User ID or Password");
            loginErrorLabel.setVisible(true);
        } else {
            appController.setLoginUser(loginUser);
            appController.setState(State.VIEW_USER_WORKLOAD);
            appController.loadScene("my-workload.fxml");
        }
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

}
