package com.uon.uonwams.controllers;

import com.uon.uonwams.models.User;
import com.uon.uonwams.models.UserManagement;
import com.uon.uonwams.data.Data;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Optional;

public class ProfileViewController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private Label profileViewLabel;

    @FXML
    private Label profileViewUserIdLabel;

    @FXML
    private Label profileViewNameLabel;

    @FXML
    private Label profileViewEmailLabel;

    @FXML
    private Label profileViewFteRatioLabel;

    @FXML
    private Label profileViewSubjectAreaLabel;

    @FXML
    private Label profileViewLineManagerLabel;


    public void setup() {
        this.setMenuAppController(appController);
        UserManagement um = new UserManagement(appController.getLoginUser());
        appController.setUserManagement(um);
        profileViewLabel.setText("View Profile");

        User loginUser = appController.getLoginUser();
        User finalLoginUser = loginUser;
        Optional<User> newLoginUser = Data.userData.getUsers().stream().filter(user -> user.getUserId() == finalLoginUser.getUserId()).findFirst();
        if (newLoginUser.isPresent()) {
            loginUser = newLoginUser.get();
            appController.setLoginUser(loginUser);
        }
        profileViewUserIdLabel.setText(Integer.toString(loginUser.getUserId()));
        profileViewNameLabel.setText(loginUser.getName());
        profileViewEmailLabel.setText(loginUser.getEmail());
        profileViewFteRatioLabel.setText(Double.toString(loginUser.getFteRatio()));
        profileViewSubjectAreaLabel.setText(loginUser.getSubjectArea());
        Optional<User> lineManagerUser = Data.userData.getUsers().stream().filter(user -> ((Integer) user.getUserId()).equals(finalLoginUser.getLineManagerUserId())).findFirst();
        profileViewLineManagerLabel.setText(lineManagerUser.isPresent() ? lineManagerUser.get().getName() : "-");
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    protected void onClickChangePasswordButton() throws IOException {
        appController.loadScene("change-password.fxml");
    }

    @FXML
    protected void onClickEditProfileButton() throws IOException {
        appController.loadScene("profile-form.fxml");
    }
}
