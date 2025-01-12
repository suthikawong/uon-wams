/**
 Program: UON WAMS Application
 Filename: ActivityFormController.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.controllers;

import com.uon.uonwams.data.Data;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ConfigurationFormController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private TextField configurationFormTotalFullTimeHoursTextField;

    @FXML
    private Label configurationFormErrorLabel;

    public void setup() {
        // add menu controller to manage page permission
        this.setMenuAppController(appController);
        configurationFormErrorLabel.setVisible(false);

        // fill the form with the current configuration
        configurationFormTotalFullTimeHoursTextField.setText(Integer.toString(Data.configurationData.getTotalFullTimeHours()));
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    // when back button is clicked, navigate to the configuration-view.fxml
    @FXML
    protected void onClickBackButton() throws IOException {
        appController.loadScene("configuration-view.fxml");
    }

    // when save button is clicked, save data and navigate to the configuration-view.fxml
    @FXML
    protected void onClickSaveButton() throws IOException {
        // validate configuration data
        // if they are invalid, exit the function and display error message on the page
        boolean isValid = validateFields();
        if (!isValid) return;

        // update configuration
        Data.configurationData.updateTotalFullTimeHours(Integer.parseInt(configurationFormTotalFullTimeHoursTextField.getText()));

        // navigate back to the configuration-view.fxml
        appController.loadScene("configuration-view.fxml");
    }

    private boolean validateFields() {
        // check are fields empty
        boolean isEmpty = checkFieldsEmpty();
        if (isEmpty) {
            configurationFormErrorLabel.setText("Please fill all required fields");
            configurationFormErrorLabel.setVisible(true);
            return false;
        }

        // check is total full-time hours valid
        try {
            Integer.parseInt(configurationFormTotalFullTimeHoursTextField.getText());
        } catch (Exception e) {
            configurationFormErrorLabel.setText("Invalid field value for field \"Total Full Time Hours\"");
            configurationFormErrorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    // check are fields empty
    private boolean checkFieldsEmpty() {
        return configurationFormTotalFullTimeHoursTextField.getText().isEmpty();
    }
}
