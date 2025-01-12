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
import com.uon.uonwams.models.Activity;
import com.uon.uonwams.models.ActivityType;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityFormController extends MenuController implements ControllerInterface {
    AppController appController;

    private boolean isEdit = false;

    @FXML
    private Label activityFormLabel;

    @FXML
    private TextField activityFormNameTextField;

    @FXML
    private ChoiceBox activityFormTypeChoiceBox;

    @FXML
    private TextField activityFormDescriptionTextField;

    @FXML
    private TextField activityFormYearTextField;

    @FXML
    private TextField activityFormDurationTextField;

    @FXML
    private TextField activityFormNoOfInstancesTextField;

    @FXML
    private Label activityFormErrorLabel;


    public void setup() {
        // add menu controller to manage page permission
        this.setMenuAppController(appController);
        activityFormErrorLabel.setVisible(false);

        // if activity is selected, the current form is for edit activity
        this.isEdit = appController.getSelectedActivity() != null;

        // initialize activity type options
        List<String> options = new ArrayList<>();
        for (ActivityType type: Data.activityTypeData.getActivityTypes()) {
            options.add(type.getName());
        }
        activityFormTypeChoiceBox.getItems().addAll(options);

        // if form is in editing state, fill the form with the selected activity information
        if (this.isEdit) {
            Activity selectedActivity = appController.getSelectedActivity();
            activityFormLabel.setText("Edit Activity");
            activityFormNameTextField.setText(selectedActivity.getActivityName());
            activityFormDescriptionTextField.setText(selectedActivity.getDescription());
            activityFormYearTextField.setText(selectedActivity.getYear());
            activityFormDurationTextField.setText(Integer.toString(selectedActivity.getDuration()));
            activityFormNoOfInstancesTextField.setText(Integer.toString(selectedActivity.getNoOfInstances()));
            StringProperty defaultChoice = new javafx.beans.property.SimpleStringProperty(selectedActivity.getActivityType());
            activityFormTypeChoiceBox.valueProperty().bindBidirectional(defaultChoice);
        } else {
            activityFormLabel.setText("Add Activity");
        }
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    // when back button is clicked, navigate to the activity-view.fxml
    @FXML
    protected void onClickBackButton() throws IOException {
        appController.loadScene("activity-view.fxml");
    }

    // when save button is clicked, save data and navigate to the activity-view.fxml
    @FXML
    protected void onClickSaveButton() throws IOException {
        // validate activity data
        // if they are invalid, exit the function and display error message on the page
        boolean isValid = validateFields();
        if (!isValid) return;

        if (this.isEdit) {
            // update activity
            appController.getWorkload().updateActivity(
                appController.getSelectedActivity().getActivityId(),
                activityFormNameTextField.getText(),
                activityFormTypeChoiceBox.getValue().toString(),
                activityFormDescriptionTextField.getText(),
                appController.getWorkloadUser().getUserId(),
                activityFormYearTextField.getText(),
                Integer.parseInt(activityFormDurationTextField.getText()),
                Integer.parseInt(activityFormNoOfInstancesTextField.getText())
            );
        } else {
            // add a new activity
            appController.getWorkload().addActivity(
                    activityFormNameTextField.getText(),
                    activityFormTypeChoiceBox.getValue().toString(),
                    activityFormDescriptionTextField.getText(),
                    appController.getWorkloadUser().getUserId(),
                    activityFormYearTextField.getText(),
                    Integer.parseInt(activityFormDurationTextField.getText()),
                    Integer.parseInt(activityFormNoOfInstancesTextField.getText())
            );
        }
        // reset selected activity to null and navigate back to the activity-view.fxml
        appController.setSelectedActivity(null);
        appController.loadScene("activity-view.fxml");
    }

    private boolean validateFields() {
        // check are fields empty
        boolean isEmpty = checkFieldsEmpty();
        if (isEmpty) {
            activityFormErrorLabel.setText("Please fill all required fields");
            activityFormErrorLabel.setVisible(true);
            return false;
        }

        // check is activity duration valid
        try {
            Integer.parseInt(activityFormDurationTextField.getText());
        } catch (Exception e) {
            activityFormErrorLabel.setText("Invalid field value for field \"Activity Duration\"");
            activityFormErrorLabel.setVisible(true);
            return false;
        }

        // check is no. of instances valid
        try {
            Integer.parseInt(activityFormNoOfInstancesTextField.getText());
        } catch (Exception e) {
            activityFormErrorLabel.setText("Invalid field value for field \"No. of Instances\"");
            activityFormErrorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    // check are fields empty
    private boolean checkFieldsEmpty() {
        return activityFormNameTextField.getText().isEmpty() ||
                activityFormTypeChoiceBox.getValue() == null ||
                activityFormDescriptionTextField.getText().isEmpty() ||
                activityFormYearTextField.getText().isEmpty() ||
                activityFormDurationTextField.getText().isEmpty() ||
                activityFormNoOfInstancesTextField.getText().isEmpty();
    }
}
