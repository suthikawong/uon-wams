package com.uon.uonwams.controllers;

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
        loginUserIdErrorLabel.setVisible(false);
        loginErrorLabel.setVisible(false);

        boolean isValid = validateFields();
        if (!isValid) return;

        int userId = Integer.parseInt(loginUserIdTextField.getText());
        String password = loginPasswordTextField.getText();

        User loginUser = new User().login(userId, password);
        if (loginUser == null) {
            loginErrorLabel.setText("Incorrect User ID or Password");
            loginErrorLabel.setVisible(true);
        } else if (loginUser.getIsAdmin()) {
            appController.setLoginUser(loginUser);
            appController.loadScene("workload.fxml");
        } else {
            appController.setLoginUser(loginUser);
            appController.loadScene("my-workload.fxml");
        }
    }

    private boolean validateFields() {
        boolean isEmpty = checkFieldsEmpty();
        if (isEmpty) {
            loginErrorLabel.setText("Please enter User ID and Password");
            loginErrorLabel.setVisible(true);
            return false;
        }

        try {
            Integer.parseInt(loginUserIdTextField.getText());
        } catch(Exception e) {
            loginUserIdErrorLabel.setText("Invalid User ID");
            loginUserIdErrorLabel.setVisible(true);
            return false;
        }

        return true;
    }

    private boolean checkFieldsEmpty() {
        return loginUserIdTextField.getText().isEmpty() ||
                loginPasswordTextField.getText().isEmpty();
    }

    @FXML
    protected void onClickForgotPassword(ActionEvent event) throws IOException {
        appController.loadScene("forgot-password.fxml");
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

}
