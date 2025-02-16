/**
 Program: UON WAMS Application
 Filename: MyWorkloadController.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.controllers;

import com.uon.uonwams.data.Data;
import com.uon.uonwams.models.Activity;
import com.uon.uonwams.models.UserWorkloadAllocation;
import com.uon.uonwams.models.Workload;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.LinkedHashMap;
import java.util.List;

public class MyWorkloadController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private TableView myWorkloadTableView;

    @FXML
    private TableView myActivityTableView;

    public void setup() {
        // add menu controller to manage page permission
        this.setMenuAppController(appController);

        // initialize Workload and UserWorkloadAllocation from logged-in user
        Workload workload = new Workload(appController.getLoginUser());
        UserWorkloadAllocation workloadUser = new UserWorkloadAllocation(appController.getLoginUser());
        appController.setWorkload(workload);
        appController.setWorkloadUser(workloadUser);

        // display workload allocations and activities in tables
        createUserWorkloadTable();
        createActivityTable();

    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    // display workload allocations in the table
    private void createUserWorkloadTable() {
        TableColumn<UserWorkloadAllocation, String> idColumn = new TableColumn<>("User ID");
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getUserId())));

        TableColumn<UserWorkloadAllocation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<UserWorkloadAllocation, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<UserWorkloadAllocation, String> fteRatioColumn = new TableColumn<>("FTE Ratio");
        fteRatioColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Double.toString(data.getValue().getFteRatio())));

        TableColumn<UserWorkloadAllocation, String> subjectAreaColumn = new TableColumn<>("Subject Area");
        subjectAreaColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSubjectArea()));

        TableColumn<UserWorkloadAllocation, String> totalHoursColumn = new TableColumn<>("Total Hours");
        totalHoursColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getUserId())));

        TableColumn<UserWorkloadAllocation, String> fteHoursColumn = new TableColumn<>("FTE Hours");
        fteHoursColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getFteHours())));

        TableColumn<UserWorkloadAllocation, String> percentTotalHoursColumn = new TableColumn<>("Parcentage of Total Hours Allocated");
        percentTotalHoursColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Double.toString(data.getValue().getParcentageOfTotalHoursAllocated())));
        percentTotalHoursColumn.setCellFactory(column -> new javafx.scene.control.TableCell<UserWorkloadAllocation, String>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    // reset text and style
                    setText(null);
                    setStyle("");
                } else {
                    double percent = Double.parseDouble(value);
                    setText(value.toString());

                    // change color based on percentage
                    if (percent < 70) {
                        setStyle("-fx-background-color: lightgreen; -fx-text-fill: black;");
                    } else if (percent < 90) {
                        setStyle("-fx-background-color: sandybrown; -fx-text-fill: black;");
                    } else {
                        setStyle("-fx-background-color: lightcoral; -fx-text-fill: black;");
                    }
                }
            }
        });

        // add columns to TableView
        myWorkloadTableView.getColumns().addAll(
                idColumn,
                nameColumn,
                emailColumn,
                fteRatioColumn,
                subjectAreaColumn,
                totalHoursColumn,
                fteHoursColumn,
                percentTotalHoursColumn
        );

        ObservableList<UserWorkloadAllocation> list = FXCollections.observableArrayList();
        // find workload allocation of the logged-in user
        list.add(appController.getWorkloadUser());
        // add data to TableView
        myWorkloadTableView.setItems(list);
    }

    // display activities of the selected user in the table
    private void createActivityTable() {
        // find activities of the logged-in user
        List<Activity> activities = appController.getWorkload().getActivitiesByUserId(appController.getLoginUser().getUserId());

        TableColumn<Activity, String> activityIdColumn = new TableColumn<>("Activity ID");
        activityIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getActivityId())));

        TableColumn<Activity, String> activityTypeColumn = new TableColumn<>("Activity Type");
        activityTypeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getActivityType()));

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

        // add columns to TableView
        myActivityTableView.getColumns().addAll(
                activityIdColumn,
                activityTypeColumn,
                activityNameColumn,
                descriptionColumn,
                responsibleUserIdColumn,
                responsibleUserNameColumn,
                yearColumn,
                durationColumn,
                noOfInstancesColumn,
                hoursColumn
        );

        if (Data.configurationData.getActivityTypes().size() > 0) {
            LinkedHashMap<String, Double> formula = Data.configurationData.getActivityTypes().getFirst().getFormula();
            for(String key : formula.keySet()) {
                TableColumn<Activity, String> column = new TableColumn<>(key);
                column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Double.toString(data.getValue().getWorkloadHours().get(key))));
                // add columns to TableView
                myActivityTableView.getColumns().add(column);
            }
        }

        ObservableList<Activity> list = FXCollections.observableArrayList();
        list.addAll(activities);
        // add data to TableView
        myActivityTableView.setItems(list);
    }
}
