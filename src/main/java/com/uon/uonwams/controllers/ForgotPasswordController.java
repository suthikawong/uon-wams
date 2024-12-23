package com.uon.uonwams.controllers;

import com.uon.uonwams.config.State;
import com.uon.uonwams.models.User;
import com.uon.uonwams.models.UserManagement;
import com.uon.uonwams.models.WAMSApplication;
import com.uon.uonwams.models.EmailUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class ForgotPasswordController implements ControllerInterface {
    private AppController appController;
    private User user = null;

    @FXML
    private TextField forgotPasswordUserIdTextField;

    @FXML
    private Label forgotPasswordUserIdErrorLabel;


    public void setup() {
        forgotPasswordUserIdErrorLabel.setVisible(false);
    }

    @FXML
    protected void onClickBackButton() throws IOException {
        appController.setState(State.LOGIN);
        appController.loadScene("login.fxml");
    }

    @FXML
    protected void onClickSendEmailButton(ActionEvent event) {
        boolean isValid = validateFields();
        if (!isValid || user == null) {
            return;
        }

        String password;
        try {
            UserManagement um = new UserManagement(user);
            password = um.forgotPassword(user);
        } catch (Exception e) {
            forgotPasswordUserIdErrorLabel.setText("Cannot reset password. Please try again.");
            forgotPasswordUserIdErrorLabel.setVisible(true);
            return;
        }

        try {
            EmailUtil emailUtil = new EmailUtil();
            emailUtil.sendEmail(user.getEmail(),"Forget password email", "Your new password is \"" + password + "\" ");
            sendEmailDialog();
        } catch (Exception e) {
            forgotPasswordUserIdErrorLabel.setText("Something went wrong. Please try again.");
            forgotPasswordUserIdErrorLabel.setVisible(true);
        }
    }



    private void sendEmailDialog() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // block interaction with the main window
        popupStage.initOwner(appController.getStage());

        Label message = new Label("Temporary password was sent to your email.");
        Button cancelButton = new Button("Close");

        cancelButton.setOnAction(e -> {
            popupStage.close();
            try {
                appController.setState(State.LOGIN);
                appController.loadScene("login.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        HBox buttonLayout = new HBox(10, cancelButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        VBox popupLayout = new VBox(20, message, buttonLayout);
        popupLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        Scene popupScene = new Scene(popupLayout, 300, 150);
        popupStage.setTitle("Confirm Delete");
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    private boolean validateFields() {
        boolean isEmpty = checkFieldsEmpty();
        int userId;
        if (isEmpty) {
            forgotPasswordUserIdErrorLabel.setText("Please enter your User ID");
            forgotPasswordUserIdErrorLabel.setVisible(true);
            return false;
        }
        try {
            userId = Integer.parseInt(forgotPasswordUserIdTextField.getText());
        } catch (Exception e) {
            forgotPasswordUserIdErrorLabel.setText("Invalid value for \"User ID\"");
            forgotPasswordUserIdErrorLabel.setVisible(true);
            return false;
        }
        for (User user: WAMSApplication.userData.getUsers()) {
            if (user.getUserId() == userId) {
                this.user = user;
                return true;
            }
        }
        forgotPasswordUserIdErrorLabel.setText("User ID not exist");
        forgotPasswordUserIdErrorLabel.setVisible(true);
        return false;
    }

    private boolean checkFieldsEmpty() {
        return forgotPasswordUserIdTextField.getText().isEmpty();
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

}
