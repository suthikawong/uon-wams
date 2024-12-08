package com.uon.uonwams.controllers;

import com.uon.uonwams.config.State;
import com.uon.uonwams.models.Activity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.List;

public class ActivityViewController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private TableView activityTableView;

    public void setup() {
        this.setMenuAppController(appController);
        createActivityTable();
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    protected void onClickBackButton() throws IOException {
        appController.setWorkloadUser(null);
        appController.setState(State.VIEW_USER_WORKLOAD);
        appController.loadScene("workload.fxml");
    }

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
        appController.createEditDeleteButtons(actionColumn, ActivityViewController::handleClickEditButton, ActivityViewController::handleClickDeleteButton);

        // Add Columns to TableView
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

        List<Activity> activities = appController.getWorkload().getActivitiesByUserId(appController.getWorkloadUser().getUserId());
        ObservableList<Activity> list = FXCollections.observableArrayList();
        for(Activity activity: activities) {
            list.add(activity);
        }
        activityTableView.setItems(list);
    }

    private static <T> void handleClickEditButton(T data, AppController appController) {
        if (Activity.class.isInstance(data)) {
            Activity activity = Activity.class.cast(data);
            System.out.println("edit: " + activity);
//            appController.getWorkload().updateActivity(
//                    activity.getActivityId(),
//                    activity.getActivityName(),
//                    activity.getActivityType().label,
//                    activity.getDescription(),
//                    activity.getResponsibleUserId(),
//                    activity.getYear(),
//                    activity.getDuration(),
//                    activity.getNoOfInstances()
//            );
        }
    }

    private static <T> void handleClickDeleteButton(T data, AppController appController) {
        if (Activity.class.isInstance(data)) {
            Activity activity = Activity.class.cast(data);
            System.out.println("delete: " + activity);
//            appController.getWorkload().deleteActivity(activity.getActivityId());
        }
    }
}
