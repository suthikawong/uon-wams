package com.uon.uonwams.controllers;

import com.uon.uonwams.models.User;
import com.uon.uonwams.data.Data;
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

public class UserFormController extends MenuController implements ControllerInterface {
    AppController appController;

    private boolean isEdit = false;

    @FXML
    private Label userFormLabel;

    @FXML
    private TextField userFormIdTextField;

    @FXML
    private TextField userFormNameTextField;

    @FXML
    private TextField userFormEmailTextField;

    @FXML
    private TextField userFormFteRatioTextField;

    @FXML
    private TextField userFormSubjectAreaTextField;

    @FXML
    private CheckBox userFormLineManagerCheckBox;

    @FXML
    private ChoiceBox userFormLineManagerChoiceBox;

    @FXML
    private Label userFormErrorLabel;


    public void setup() {
        this.setMenuAppController(appController);
        userFormErrorLabel.setVisible(false);
        this.isEdit = appController.getSelectedUser() != null;
        List<User> options = new ArrayList<>();
        for (User user: Data.userData.getUsers()) {
            if (!appController.getLoginUser().getIsAdmin() && appController.getSelectedUser() != null && user.getUserId() == appController.getSelectedUser().getUserId()) continue;
            options.add(user);
        }
        userFormLineManagerChoiceBox.getItems().addAll(options);
        userFormLineManagerChoiceBox.disableProperty().bind(userFormLineManagerCheckBox.selectedProperty());
        userFormLineManagerChoiceBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User object) {
                if (object == null) return "";
                return object.getName();
            }

            @Override
            public User fromString(String s) {
                return null;
            }
        });

        if (this.isEdit) {
            User selectedUser = appController.getSelectedUser();
            userFormLabel.setText("Edit User");
            userFormIdTextField.setText(Integer.toString(selectedUser.getUserId()));
            userFormIdTextField.setDisable(true);
            userFormNameTextField.setText(selectedUser.getName());
            userFormEmailTextField.setText(selectedUser.getEmail());
            userFormFteRatioTextField.setText(Double.toString(selectedUser.getFteRatio()));
            userFormSubjectAreaTextField.setText(selectedUser.getSubjectArea());
            if (selectedUser.getLineManagerUserId() != null) {
                userFormLineManagerCheckBox.setSelected(false);
                Optional<User> lineManager = options.stream().filter(user -> user.getUserId() == selectedUser.getLineManagerUserId()).findFirst();
                if (lineManager.isPresent()) {
                    userFormLineManagerChoiceBox.setValue(lineManager.get());
                }
            } else userFormLineManagerCheckBox.setSelected(true);
        } else {
            userFormLabel.setText("Add User");
        }

    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    protected void onClickBackButton() throws IOException {
        appController.loadScene("user-view.fxml");
    }

    @FXML
    protected void onClickSaveButton() throws IOException {
        boolean isValid = validateFields();
        if (!isValid) return;

        User lineManager = (User) userFormLineManagerChoiceBox.getValue();
        if (this.isEdit) {
            appController.getUserManagement().updateUser(
                    appController.getSelectedUser().getUserId(),
                    userFormNameTextField.getText(),
                    userFormEmailTextField.getText(),
                    Double.parseDouble(userFormFteRatioTextField.getText()),
                    userFormSubjectAreaTextField.getText(),
                    userFormLineManagerCheckBox.isSelected() ? null : lineManager.getUserId()
            );
        } else {
            try {
                appController.getUserManagement().addUser(
                        Integer.parseInt(userFormIdTextField.getText()),
                        userFormNameTextField.getText(),
                        userFormEmailTextField.getText(),
                        Double.parseDouble(userFormFteRatioTextField.getText()),
                        userFormSubjectAreaTextField.getText(),
                        userFormLineManagerCheckBox.isSelected() ? null : lineManager.getUserId()
                );
            } catch (Exception e) {
                userFormErrorLabel.setText(e.getMessage());
                userFormErrorLabel.setVisible(true);
                return;
            }
        }
        appController.setSelectedUser(null);
        appController.loadScene("user-view.fxml");
    }

    private boolean validateFields() {
        boolean isEmpty = checkFieldsEmpty();
        int userId;
        if (isEmpty) {
            userFormErrorLabel.setText("Please fill all required fields");
            userFormErrorLabel.setVisible(true);
            return false;
        }
        try {
            userId = Integer.parseInt(userFormIdTextField.getText());
        } catch (Exception e) {
            userFormErrorLabel.setText("Invalid field value for field \"User ID\"");
            userFormErrorLabel.setVisible(true);
            return false;
        }
        if (!this.isEdit) {
            for (User user: Data.userData.getUsers()) {
                if (user.getUserId() == userId) {
                    userFormErrorLabel.setText("This User ID already exists");
                    userFormErrorLabel.setVisible(true);
                    return false;
                }
            }
        }
        try {
            Double.parseDouble(userFormFteRatioTextField.getText());
        } catch (Exception e) {
            userFormErrorLabel.setText("Invalid field value for field \"FTE Ratio\"");
            userFormErrorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    private boolean checkFieldsEmpty() {
        return userFormIdTextField.getText().isEmpty() ||
                userFormNameTextField.getText().isEmpty() ||
                userFormEmailTextField.getText().isEmpty() ||
                userFormFteRatioTextField.getText().isEmpty() ||
                userFormSubjectAreaTextField.getText().isEmpty() ||
                (!userFormLineManagerCheckBox.isSelected() && userFormLineManagerChoiceBox.getValue() == null);
    }
}
