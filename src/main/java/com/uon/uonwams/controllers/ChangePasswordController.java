/**
 Program: UON WAMS Application
 Filename: ChangePasswordController.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ChangePasswordController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private TextField changePasswordNewPasswordTextField;

    @FXML
    private TextField changePasswordConfirmPasswordTextField;

    @FXML
    private Label changePasswordErrorLabel;


    public void setup() {
        // add menu controller to manage page permission
        this.setMenuAppController(appController);
        changePasswordErrorLabel.setVisible(false);

    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    // when back button is clicked, navigate to the profile-view.fxml
    @FXML
    protected void onClickBackButton() throws IOException {
        appController.loadScene("profile-view.fxml");
    }

    // when save button is clicked, save data and navigate to the profile-view.fxml
    @FXML
    protected void onClickSaveButton() {
        // validate passwords
        // if they are invalid, exit the function and display error message on the page
        boolean isValid = validateFields();
        if (!isValid) return;

        try {
            // change password and navigate back to the profile-view.fxml
            appController.getLoginUser().changePassword(changePasswordNewPasswordTextField.getText());
            appController.loadScene("profile-view.fxml");
        } catch (Exception e) {
            changePasswordErrorLabel.setText("Cannot updated password, please try again");
            changePasswordErrorLabel.setVisible(true);
        }
    }

    private boolean validateFields() {
        // check are fields empty
        boolean isEmpty = checkFieldsEmpty();
        if (isEmpty) {
            changePasswordErrorLabel.setText("Please fill all required fields");
            changePasswordErrorLabel.setVisible(true);
            return false;
        }

        // check are password match
        boolean isMatch = checkPasswordMatch();
        if (!isMatch) {
            changePasswordErrorLabel.setText("Password does not match");
            changePasswordErrorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    // check are fields empty
    private boolean checkFieldsEmpty() {
        return changePasswordNewPasswordTextField.getText().isEmpty() ||
                changePasswordConfirmPasswordTextField.getText().isEmpty();
    }

    // check are password match
    private boolean checkPasswordMatch() {
        return changePasswordNewPasswordTextField.getText().equals(changePasswordConfirmPasswordTextField.getText());
    }
}
