package com.uon.uonwams.controllers;

import com.uon.uonwams.models.Activity;
import com.uon.uonwams.models.UserWorkloadAllocation;
import com.uon.uonwams.models.Workload;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class WorkloadController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private MenuItem workloadMenuItem;

    @FXML
    private TableView workloadTableView;

//    @FXML
//    private TableView activityTableView;

    public void setup() {
        // check user's role
//        workloadMenuItem.setVisible(false);
        this.setMenuAppController(appController);

        Workload workload = new Workload(appController.getLoginUser());
        UserWorkloadAllocation workloadUser = new UserWorkloadAllocation(appController.getLoginUser());
        appController.setWorkload(workload);
        appController.setWorkloadUser(workloadUser);
        createUserWorkloadTable();
//        createActivityTable();

    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    private void createUserWorkloadTable() {
        TableColumn<UserWorkloadAllocation, String> idColumn = new TableColumn<>("User ID");
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getUserId())));

        TableColumn<UserWorkloadAllocation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<UserWorkloadAllocation, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<UserWorkloadAllocation, String> fteRatioColumn = new TableColumn<>("FTE Ratio");
        fteRatioColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Float.toString(data.getValue().getFteRatio())));

        TableColumn<UserWorkloadAllocation, String> subjectAreaColumn = new TableColumn<>("Subject Area");
        subjectAreaColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSubjectArea()));

        TableColumn<UserWorkloadAllocation, String> totalHoursColumn = new TableColumn<>("totalHours");
        totalHoursColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getUserId())));

        TableColumn<UserWorkloadAllocation, String> fteHoursColumn = new TableColumn<>("FTE Hours");
        fteHoursColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getFteHours())));

        TableColumn<UserWorkloadAllocation, String> totalAtsrTsColumn = new TableColumn<>("Total ATSR + TS");
        totalAtsrTsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getTotalAtsrTs())));

        TableColumn<UserWorkloadAllocation, String> percentAtsrColumn = new TableColumn<>("Parcentage of ATSR allocated");
        percentAtsrColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Double.toString(data.getValue().getPercentageOfAtsrAllocated())));

        TableColumn<UserWorkloadAllocation, String> percentTotalHoursColumn = new TableColumn<>("Parcentage of Total Hours Allocated");
        percentTotalHoursColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Double.toString(data.getValue().getParcentageOfTotalHoursAllocated())));

        TableColumn<UserWorkloadAllocation, String> fteAtsrHoursColumn = new TableColumn<>("FTE ATSR Hours");
        fteAtsrHoursColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getFteAtsrHours())));


        // Add Columns to TableView
        workloadTableView.getColumns().add(idColumn);
        workloadTableView.getColumns().add(nameColumn);
        workloadTableView.getColumns().add(emailColumn);
        workloadTableView.getColumns().add(fteRatioColumn);
        workloadTableView.getColumns().add(subjectAreaColumn);
        workloadTableView.getColumns().add(totalHoursColumn);
        workloadTableView.getColumns().add(fteHoursColumn);
        workloadTableView.getColumns().add(totalAtsrTsColumn);
        workloadTableView.getColumns().add(percentAtsrColumn);
        workloadTableView.getColumns().add(percentTotalHoursColumn);
        workloadTableView.getColumns().add(fteAtsrHoursColumn);

        ObservableList<UserWorkloadAllocation> list = FXCollections.observableArrayList();
        list.addAll(appController.getWorkload().getUserWorkloadAllocation());
        workloadTableView.setItems(list);
    }


//    private void createActivityTable() {
//        TableColumn<Activity, String> activityIdColumn = new TableColumn<>("Activity ID");
//        activityIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getActivityId())));
//
//        TableColumn<Activity, String> activityTypeColumn = new TableColumn<>("Activity Type");
//        activityTypeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getActivityType().label));
//
//        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Activity Name");
//        activityNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getActivityName()));
//
//        TableColumn<Activity, String> descriptionColumn = new TableColumn<>("Description");
//        descriptionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
//
//        TableColumn<Activity, String> responsibleUserIdColumn = new TableColumn<>("Responsible User ID");
//        responsibleUserIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getResponsibleUserId())));
//
//        TableColumn<Activity, String> responsibleUserNameColumn = new TableColumn<>("Responsible User Name");
//        responsibleUserNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getResponsibleUser()));
//
//        TableColumn<Activity, String> yearColumn = new TableColumn<>("Year");
//        yearColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getYear()));
//
//        TableColumn<Activity, String> durationColumn = new TableColumn<>("Duration");
//        durationColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getDuration())));
//
//        TableColumn<Activity, String> noOfInstancesColumn = new TableColumn<>("No. of instances");
//        noOfInstancesColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getNoOfInstances())));
//
//        TableColumn<Activity, String> hoursColumn = new TableColumn<>("Hours");
//        hoursColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getHours())));
//
//        TableColumn<Activity, String> atsrColumn = new TableColumn<>("ATSR");
//        atsrColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getATSR())));
//
//        TableColumn<Activity, String> tsColumn = new TableColumn<>("TS");
//        tsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getTS())));
//
//        TableColumn<Activity, String> tlrColumn = new TableColumn<>("TLR");
//        tlrColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getTLR())));
//
//        TableColumn<Activity, String> saColumn = new TableColumn<>("SA");
//        saColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getSA())));
//
//        TableColumn<Activity, String> otherColumn = new TableColumn<>("Other");
//        otherColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getOther())));
//
//
//        // Add Columns to TableView
//        activityTableView.getColumns().add(activityIdColumn);
//        activityTableView.getColumns().add(activityTypeColumn);
//        activityTableView.getColumns().add(activityNameColumn);
//        activityTableView.getColumns().add(descriptionColumn);
//        activityTableView.getColumns().add(responsibleUserIdColumn);
//        activityTableView.getColumns().add(responsibleUserNameColumn);
//        activityTableView.getColumns().add(yearColumn);
//        activityTableView.getColumns().add(durationColumn);
//        activityTableView.getColumns().add(noOfInstancesColumn);
//        activityTableView.getColumns().add(hoursColumn);
//        activityTableView.getColumns().add(atsrColumn);
//        activityTableView.getColumns().add(tsColumn);
//        activityTableView.getColumns().add(tlrColumn);
//        activityTableView.getColumns().add(saColumn);
//        activityTableView.getColumns().add(otherColumn);
//
//
//        List<Activity> activities = appController.getWorkload().getActivitiesByUserId(appController.getLoginUser().getUserId());
//        ObservableList<Activity> list = FXCollections.observableArrayList();
//        for(Activity activity: activities) {
//            list.add(activity);
//        }
//        activityTableView.setItems(list);
//    }
}
