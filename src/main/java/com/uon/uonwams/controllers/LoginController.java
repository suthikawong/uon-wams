/**
 Program: UON WAMS Application
 Filename: LoginController.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

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


    // when login button is clicked, checking whether User ID and password match
    @FXML
    protected void onClickLoginButton(ActionEvent event) throws IOException {
        loginUserIdErrorLabel.setVisible(false);
        loginErrorLabel.setVisible(false);

        // validate fields
        // if they are invalid, exit the function and display error message on the page
        boolean isValid = validateFields();
        if (!isValid) return;

        int userId = Integer.parseInt(loginUserIdTextField.getText());
        String password = loginPasswordTextField.getText();

        // login
        User loginUser = new User().login(userId, password);
        if (loginUser == null) { // if userId and password not match
            loginErrorLabel.setText("Incorrect User ID or Password");
            loginErrorLabel.setVisible(true);
        } else if (loginUser.getIsAdmin()) { // if userId and password match with admin user
            appController.setLoginUser(loginUser);
            appController.loadScene("workload.fxml");
        } else { // if there is user that match
            appController.setLoginUser(loginUser);
            appController.loadScene("my-workload.fxml");
        }
    }

    private boolean validateFields() {
        // check are fields empty
        boolean isEmpty = checkFieldsEmpty();
        if (isEmpty) {
            loginErrorLabel.setText("Please enter User ID and Password");
            loginErrorLabel.setVisible(true);
            return false;
        }

        // check is userId valid
        try {
            Integer.parseInt(loginUserIdTextField.getText());
        } catch(Exception e) {
            loginUserIdErrorLabel.setText("Invalid User ID");
            loginUserIdErrorLabel.setVisible(true);
            return false;
        }

        return true;
    }

    // check are fields empty
    private boolean checkFieldsEmpty() {
        return loginUserIdTextField.getText().isEmpty() ||
                loginPasswordTextField.getText().isEmpty();
    }

    // when forgot password button is clicked, navigate to the forgot-password.fxml
    @FXML
    protected void onClickForgotPassword(ActionEvent event) throws IOException {
        appController.loadScene("forgot-password.fxml");
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

}
