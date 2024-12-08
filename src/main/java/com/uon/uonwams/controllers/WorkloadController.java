package com.uon.uonwams.controllers;

import com.uon.uonwams.config.State;
import com.uon.uonwams.models.UserWorkloadAllocation;
import com.uon.uonwams.models.Workload;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;

public class WorkloadController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private TableView workloadTableView;

    public void setup() {
        this.setMenuAppController(appController);

        Workload workload = new Workload(appController.getLoginUser());
        UserWorkloadAllocation workloadUser = new UserWorkloadAllocation(appController.getLoginUser());
        appController.setWorkload(workload);
        appController.setWorkloadUser(workloadUser);
        createUserWorkloadTable();
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

        TableColumn<UserWorkloadAllocation, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("dummy"));
        appController.createActionButton(actionColumn, "View", WorkloadController::handleClickViewButton);

        // Add Columns to TableView
        workloadTableView.getColumns().addAll(
                idColumn,
                nameColumn,
                emailColumn,
                fteRatioColumn,
                subjectAreaColumn,
                totalHoursColumn,
                fteHoursColumn,
                totalAtsrTsColumn,
                percentAtsrColumn,
                percentTotalHoursColumn,
                fteAtsrHoursColumn,
                actionColumn
        );

        ObservableList<UserWorkloadAllocation> list = FXCollections.observableArrayList();
        list.addAll(appController.getWorkload().getUserWorkloadAllocation());
        workloadTableView.setItems(list);
    }

    private static <T> void handleClickViewButton(T data, AppController appController) throws IOException {
        if (UserWorkloadAllocation.class.isInstance(data)) {
            UserWorkloadAllocation userWorkload = UserWorkloadAllocation.class.cast(data);
            appController.setWorkloadUser(userWorkload);
            appController.setState(State.VIEW_WORKLOAD);
            appController.loadScene("activity-view.fxml");
        }
    }

}
