package com.uon.uonwams.controllers;

import com.uon.uonwams.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

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
        // add menu controller to manage page permission
        this.setMenuAppController(appController);
        profileFormErrorLabel.setVisible(false);

        User loginUser = appController.getLoginUser();
        profileFormLabel.setText("Edit Profile");
        // fill the form with the logged-in user information
        profileFormIdTextField.setText(Integer.toString(loginUser.getUserId()));
        profileFormIdTextField.setDisable(true);
        profileFormNameTextField.setText(loginUser.getName());
        profileFormEmailTextField.setText(loginUser.getEmail());
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
    protected void onClickSaveButton() throws IOException {
        // validate user data
        // if they are invalid, exit the function and display error message on the page
        boolean isValid = validateFields();
        if (!isValid) return;

        User loginUser = appController.getLoginUser();

        // update user
        appController.getUserManagement().updateUser(
                appController.getLoginUser().getUserId(),
                profileFormNameTextField.getText(),
                profileFormEmailTextField.getText(),
                loginUser.getFteRatio(),
                loginUser.getSubjectArea(),
                loginUser.getLineManagerUserId()
        );
        // navigate back to the profile-view.fxml
        appController.loadScene("profile-view.fxml");
    }

    private boolean validateFields() {
        // check are fields empty
        boolean isEmpty = checkFieldsEmpty();
        if (isEmpty) {
            profileFormErrorLabel.setText("Please fill all required fields");
            profileFormErrorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    // check are fields empty
    private boolean checkFieldsEmpty() {
        return profileFormIdTextField.getText().isEmpty() ||
                profileFormNameTextField.getText().isEmpty() ||
                profileFormEmailTextField.getText().isEmpty();
    }
}
