package com.uon.uonwams.controllers;

import com.uon.uonwams.models.Activity;
import com.uon.uonwams.models.UserWorkloadAllocation;
import com.uon.uonwams.models.Workload;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class MyWorkloadController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private TableView myWorkloadTableView;

    @FXML
    private TableView myActivityTableView;

    public void setup() {
        this.setMenuAppController(appController);

        Workload workload = new Workload(appController.getLoginUser());
        UserWorkloadAllocation workloadUser = new UserWorkloadAllocation(appController.getLoginUser());
        appController.setWorkload(workload);
        appController.setWorkloadUser(workloadUser);
        createUserWorkloadTable();
        createActivityTable();

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

        TableColumn<UserWorkloadAllocation, String> totalHoursColumn = new TableColumn<>("Total Hours");
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
        myWorkloadTableView.getColumns().add(idColumn);
        myWorkloadTableView.getColumns().add(nameColumn);
        myWorkloadTableView.getColumns().add(emailColumn);
        myWorkloadTableView.getColumns().add(fteRatioColumn);
        myWorkloadTableView.getColumns().add(subjectAreaColumn);
        myWorkloadTableView.getColumns().add(totalHoursColumn);
        myWorkloadTableView.getColumns().add(fteHoursColumn);
        myWorkloadTableView.getColumns().add(totalAtsrTsColumn);
        myWorkloadTableView.getColumns().add(percentAtsrColumn);
        myWorkloadTableView.getColumns().add(percentTotalHoursColumn);
        myWorkloadTableView.getColumns().add(fteAtsrHoursColumn);

        ObservableList<UserWorkloadAllocation> list = FXCollections.observableArrayList();
        list.add(appController.getWorkloadUser());
        myWorkloadTableView.setItems(list);
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


        // Add Columns to TableView
        myActivityTableView.getColumns().add(activityIdColumn);
        myActivityTableView.getColumns().add(activityTypeColumn);
        myActivityTableView.getColumns().add(activityNameColumn);
        myActivityTableView.getColumns().add(descriptionColumn);
        myActivityTableView.getColumns().add(responsibleUserIdColumn);
        myActivityTableView.getColumns().add(responsibleUserNameColumn);
        myActivityTableView.getColumns().add(yearColumn);
        myActivityTableView.getColumns().add(durationColumn);
        myActivityTableView.getColumns().add(noOfInstancesColumn);
        myActivityTableView.getColumns().add(hoursColumn);
        myActivityTableView.getColumns().add(atsrColumn);
        myActivityTableView.getColumns().add(tsColumn);
        myActivityTableView.getColumns().add(tlrColumn);
        myActivityTableView.getColumns().add(saColumn);
        myActivityTableView.getColumns().add(otherColumn);


        List<Activity> activities = appController.getWorkload().getActivitiesByUserId(appController.getLoginUser().getUserId());
        ObservableList<Activity> list = FXCollections.observableArrayList();
        for(Activity activity: activities) {
            list.add(activity);
        }
        myActivityTableView.setItems(list);
    }
}
