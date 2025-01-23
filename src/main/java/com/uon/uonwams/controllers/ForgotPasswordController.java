/**
 Program: UON WAMS Application
 Filename: ForgotPasswordController.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.controllers;

import com.uon.uonwams.models.User;
import com.uon.uonwams.data.Data;
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

import static com.uon.uonwams.models.User.resetPassword;

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

    // when back button is clicked, navigate to the login.fxml
    @FXML
    protected void onClickBackButton() throws IOException {
        appController.loadScene("login.fxml");
    }

    @FXML
    protected void onClickSendEmailButton(ActionEvent event) {
        // validate fields and userId
        // if they are invalid, exit the function and display error message on the page
        boolean isValid = validateFields();
        if (!isValid || user == null) {
            return;
        }

        String password;
        try {
            // generate new password and update it to user data
            password = resetPassword(user);
        } catch (Exception e) {
            forgotPasswordUserIdErrorLabel.setText("Cannot reset password. Please try again.");
            forgotPasswordUserIdErrorLabel.setVisible(true);
            return;
        }

        try {
            // send new password to user's email address
            EmailUtil emailUtil = new EmailUtil();
            emailUtil.sendEmail(user.getEmail(),"Forget password email", "Your new password is \"" + password + "\" ");
            // display dialog
            sendEmailDialog();
        } catch (Exception e) {
            forgotPasswordUserIdErrorLabel.setText("Something went wrong. Please try again.");
            forgotPasswordUserIdErrorLabel.setVisible(true);
        }
    }

    // show send email dialog
    private void sendEmailDialog() {
        // create dialog
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // block interaction with the main window
        popupStage.initOwner(appController.getStage());

        Label message = new Label("Temporary password was sent to your email.");
        Button cancelButton = new Button("Close");

        // attach event listener when buttons are clicked
        // click cancel: close dialog
        cancelButton.setOnAction(e -> {
            // close dialog
            popupStage.close();
            try {
                // navigate to login.fxml
                appController.loadScene("login.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // dialog layout
        HBox buttonLayout = new HBox(10, cancelButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        VBox popupLayout = new VBox(20, message, buttonLayout);
        popupLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        Scene popupScene = new Scene(popupLayout, 300, 150);

        // set scene
        popupStage.setTitle("Email was sent");
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    private boolean validateFields() {
        // check are fields empty
        boolean isEmpty = checkFieldsEmpty();
        int userId;
        if (isEmpty) {
            forgotPasswordUserIdErrorLabel.setText("Please enter your User ID");
            forgotPasswordUserIdErrorLabel.setVisible(true);
            return false;
        }

        // check is userId valid
        try {
            userId = Integer.parseInt(forgotPasswordUserIdTextField.getText());
        } catch (Exception e) {
            forgotPasswordUserIdErrorLabel.setText("Invalid value for \"User ID\"");
            forgotPasswordUserIdErrorLabel.setVisible(true);
            return false;
        }

        // check is userId exist
        for (User user: Data.userData.getUsers()) {
            if (user.getUserId() == userId) {
                this.user = user;
                return true;
            }
        }
        forgotPasswordUserIdErrorLabel.setText("User ID does not exist");
        forgotPasswordUserIdErrorLabel.setVisible(true);
        return false;
    }

    // check is field empty
    private boolean checkFieldsEmpty() {
        return forgotPasswordUserIdTextField.getText().isEmpty();
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

}
