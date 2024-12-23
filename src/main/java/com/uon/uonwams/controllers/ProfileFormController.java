package com.uon.uonwams.controllers;

import com.uon.uonwams.config.State;
import com.uon.uonwams.models.User;
import com.uon.uonwams.models.UserManagement;
import com.uon.uonwams.models.WAMSApplication;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfileFormController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private Label profileFormLabel;

    @FXML
    private TextField profileFormIdTextField;

    @FXML
    private TextField profileFormNameTextField;

    @FXML
    private TextField profileFormEmailTextField;

    @FXML
    private Label profileFormErrorLabel;


    public void setup() {
        this.setMenuAppController(appController);
        profileFormErrorLabel.setVisible(false);

        User loginUser = appController.getLoginUser();
        profileFormLabel.setText("Edit Profile");
        profileFormIdTextField.setText(Integer.toString(loginUser.getUserId()));
        profileFormIdTextField.setDisable(true);
        profileFormNameTextField.setText(loginUser.getName());
        profileFormEmailTextField.setText(loginUser.getEmail());
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    protected void onClickBackButton() throws IOException {
        appController.setState(State.VIEW_PROFILE);
        appController.loadScene("profile-view.fxml");
    }

    @FXML
    protected void onClickSaveButton() throws IOException {
        boolean isValid = validateFields();
        if (!isValid) return;

        User loginUser = appController.getLoginUser();

        appController.getUserManagement().updateUser(
                appController.getLoginUser().getUserId(),
                profileFormNameTextField.getText(),
                profileFormEmailTextField.getText(),
                loginUser.getFteRatio(),
                loginUser.getSubjectArea(),
                loginUser.getLineManagerUserId()
        );
        appController.setState(State.VIEW_PROFILE);
        appController.loadScene("profile-view.fxml");
    }

    private boolean validateFields() {
        boolean isEmpty = checkFieldsEmpty();
        if (isEmpty) {
            profileFormErrorLabel.setText("Please fill all required fields");
            profileFormErrorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    private boolean checkFieldsEmpty() {
        return profileFormIdTextField.getText().isEmpty() ||
                profileFormNameTextField.getText().isEmpty() ||
                profileFormEmailTextField.getText().isEmpty();
    }
}
