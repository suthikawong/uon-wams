package com.uon.uonwams.controllers;

import com.uon.uonwams.models.Activity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ActivityViewController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private TableView activityTableView;

    public void setup() {
        // add menu controller to manage page permission
        this.setMenuAppController(appController);
        // display activity table
        createActivityTable();
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    // when back button is clicked, navigate to the workload.fxml
    @FXML
    protected void onClickBackButton() throws IOException {
        appController.setWorkloadUser(null);
        appController.loadScene("workload.fxml");
    }

    // when add button is clicked, navigate to the activity-form.fxml
    @FXML
    protected void onClickAddButton() throws IOException {
        appController.setSelectedActivity(null);
        appController.loadScene("activity-form.fxml");
    }

    // display activities of the selected user in the table
    private void createActivityTable() {
        TableColumn<Activity, String> activityIdColumn = new TableColumn<>("Activity ID");
        activityIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getActivityId())));

        TableColumn<Activity, String> activityTypeColumn = new TableColumn<>("Activity Type");
        activityTypeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getActivityType().label));

        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Activity Name");
        activityNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getActivityName()));

        TableColumn<Activity, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));

        TableColumn<Activity, String> responsibleUserIdColumn = new TableColumn<>("Responsible User ID");
        responsibleUserIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getResponsibleUserId())));

        TableColumn<Activity, String> responsibleUserNameColumn = new TableColumn<>("Responsible User Name");
        responsibleUserNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getResponsibleUser()));

        TableColumn<Activity, String> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getYear()));

        TableColumn<Activity, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getDuration())));

        TableColumn<Activity, String> noOfInstancesColumn = new TableColumn<>("No. of instances");
        noOfInstancesColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getNoOfInstances())));

        TableColumn<Activity, String> hoursColumn = new TableColumn<>("Hours");
        hoursColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getHours())));

        TableColumn<Activity, String> atsrColumn = new TableColumn<>("ATSR");
        atsrColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getATSR())));

        TableColumn<Activity, String> tsColumn = new TableColumn<>("TS");
        tsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getTS())));

        TableColumn<Activity, String> tlrColumn = new TableColumn<>("TLR");
        tlrColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getTLR())));

        TableColumn<Activity, String> saColumn = new TableColumn<>("SA");
        saColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getSA())));

        TableColumn<Activity, String> otherColumn = new TableColumn<>("Other");
        otherColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getOther())));

        TableColumn<Activity, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("dummy"));
        // create edit and delete buttons in the table
        appController.createEditDeleteButtons(actionColumn, ActivityViewController::handleClickEditButton, ActivityViewController::handleClickDeleteButton);

        // add columns to TableView
        activityTableView.getColumns().addAll(
                activityIdColumn,
                activityTypeColumn,
                activityNameColumn,
                descriptionColumn,
                responsibleUserIdColumn,
                responsibleUserNameColumn,
                yearColumn,
                durationColumn,
                noOfInstancesColumn,
                hoursColumn,
                atsrColumn,
                tsColumn,
                tlrColumn,
                saColumn,
                otherColumn,
                actionColumn
        );

        // find activities of the selected user
        List<Activity> activities = appController.getWorkload().getActivitiesByUserId(appController.getWorkloadUser().getUserId());
        ObservableList<Activity> list = FXCollections.observableArrayList();
        for(Activity activity: activities) {
            list.add(activity);
        }
        // add data to TableView
        activityTableView.setItems(list);
    }

    // when edit button is clicked, set it as a selected activity and navigate to the activity-form.fxml
    private static <T> void handleClickEditButton(T data, AppController appController) throws IOException {
        if (Activity.class.isInstance(data)) {
            Activity activity = Activity.class.cast(data);
            appController.setSelectedActivity(activity);
            appController.loadScene("activity-form.fxml");
        }
    }

    // when delete button is clicked, display the dialog
    // activity will be deleted when clicking Delete button
    private static <T> void handleClickDeleteButton(T data, AppController appController) throws IOException {
        // create dialog
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // block interaction with the main window
        popupStage.initOwner(appController.getStage());

        Label message = new Label("Are you sure you want to delete this item?");
        Button cancelButton = new Button("Cancel");
        Button deleteButton = new Button("Delete");

        // attach event listener when buttons are clicked
        // click cancel: close dialog
        cancelButton.setOnAction(e -> popupStage.close());
        // click delete: delete that activity
        deleteButton.setOnAction(e -> {
            // delete activity
            deleteActivity(data, appController);
            // close dialog
            popupStage.close();
            try {
                // navigate to activity-view.fxml
                appController.loadScene("activity-view.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // dialog layout
        HBox buttonLayout = new HBox(10, cancelButton, deleteButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        VBox popupLayout = new VBox(20, message, buttonLayout);
        popupLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        Scene popupScene = new Scene(popupLayout, 300, 150);

        // set scene
        popupStage.setTitle("Confirm Delete");
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    // when delete button in dialog is clicked, delete the selected activity
    private static <T> void deleteActivity(T data, AppController appController) {
        if (Activity.class.isInstance(data)) {
            Activity activity = Activity.class.cast(data);
            appController.getWorkload().deleteActivity(activity.getActivityId());
        }
    }

}
