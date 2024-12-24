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
        this.setMenuAppController(appController);
        changePasswordErrorLabel.setVisible(false);

    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    protected void onClickBackButton() throws IOException {
        appController.loadScene("profile-view.fxml");
    }

    @FXML
    protected void onClickSaveButton() {
        boolean isValid = validateFields();
        if (!isValid) return;

        try {
            appController.getUserManagement().changePassword(changePasswordNewPasswordTextField.getText());
            appController.loadScene("profile-view.fxml");
        } catch (Exception e) {
            changePasswordErrorLabel.setText("Cannot updated password, please try again");
            changePasswordErrorLabel.setVisible(true);
        }
    }

    private boolean validateFields() {
        boolean isEmpty = checkFieldsEmpty();
        if (isEmpty) {
            changePasswordErrorLabel.setText("Please fill all required fields");
            changePasswordErrorLabel.setVisible(true);
            return false;
        }
        boolean isMatch = checkPasswordMatch();
        if (!isMatch) {
            changePasswordErrorLabel.setText("Password do not match");
            changePasswordErrorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    private boolean checkFieldsEmpty() {
        return changePasswordNewPasswordTextField.getText().isEmpty() ||
                changePasswordConfirmPasswordTextField.getText().isEmpty();
    }

    private boolean checkPasswordMatch() {
        return changePasswordNewPasswordTextField.getText().equals(changePasswordConfirmPasswordTextField.getText());
    }
}
