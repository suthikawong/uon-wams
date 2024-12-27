package com.uon.uonwams.controllers;

import com.uon.uonwams.configs.ActivityType;
import com.uon.uonwams.models.Activity;
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
        this.setMenuAppController(appController);
        activityFormErrorLabel.setVisible(false);
        this.isEdit = appController.getSelectedActivity() != null;
        List<String> options = new ArrayList<>();
        for (ActivityType type: ActivityType.values()) {
            options.add(type.label);
        }
        activityFormTypeChoiceBox.getItems().addAll(options);
        if (this.isEdit) {
            Activity selectedActivity = appController.getSelectedActivity();
            activityFormLabel.setText("Edit Activity");
            activityFormNameTextField.setText(selectedActivity.getActivityName());
            activityFormDescriptionTextField.setText(selectedActivity.getDescription());
            activityFormYearTextField.setText(selectedActivity.getYear());
            activityFormDurationTextField.setText(Integer.toString(selectedActivity.getDuration()));
            activityFormNoOfInstancesTextField.setText(Integer.toString(selectedActivity.getNoOfInstances()));
            StringProperty defaultChoice = new javafx.beans.property.SimpleStringProperty(selectedActivity.getActivityType().label);
            activityFormTypeChoiceBox.valueProperty().bindBidirectional(defaultChoice);
        } else {
            activityFormLabel.setText("Add Activity");
        }
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    protected void onClickBackButton() throws IOException {
        appController.loadScene("activity-view.fxml");
    }

    @FXML
    protected void onClickSaveButton() throws IOException {
        boolean isValid = validateFields();
        if (!isValid) return;

        if (this.isEdit) {
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
        appController.setSelectedActivity(null);
        appController.loadScene("activity-view.fxml");
    }

    private boolean validateFields() {
        boolean isEmpty = checkFieldsEmpty();
        if (isEmpty) {
            activityFormErrorLabel.setText("Please fill all required fields");
            activityFormErrorLabel.setVisible(true);
            return false;
        }
        try {
            Integer.parseInt(activityFormDurationTextField.getText());
        } catch (Exception e) {
            activityFormErrorLabel.setText("Invalid field value for field \"Activity Duration\"");
            activityFormErrorLabel.setVisible(true);
            return false;
        }
        try {
            Integer.parseInt(activityFormNoOfInstancesTextField.getText());
        } catch (Exception e) {
            activityFormErrorLabel.setText("Invalid field value for field \"No. of Instances\"");
            activityFormErrorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    private boolean checkFieldsEmpty() {
        return activityFormNameTextField.getText().isEmpty() ||
                activityFormTypeChoiceBox.getValue() == null ||
                activityFormDescriptionTextField.getText().isEmpty() ||
                activityFormYearTextField.getText().isEmpty() ||
                activityFormDurationTextField.getText().isEmpty() ||
                activityFormNoOfInstancesTextField.getText().isEmpty();
    }
}
