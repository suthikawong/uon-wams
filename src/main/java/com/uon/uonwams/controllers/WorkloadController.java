package com.uon.uonwams.controllers;

import com.uon.uonwams.config.State;
import com.uon.uonwams.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class WorkloadController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private TableView workloadTableView;

    @FXML
    private TextField workloadStaffSearchTextField;

    @FXML
    private ChoiceBox workloadSubjectAreaSearchChoiceBox;

    public void setup() {
        this.setMenuAppController(appController);

        List<String> options = new ArrayList<>();
        options.add("None");
        for (User user: WAMSApplication.userData.getUsers()) {
            if (!options.contains(user.getSubjectArea())) {
                options.add(user.getSubjectArea());
            }
        }
        workloadSubjectAreaSearchChoiceBox.getItems().addAll(options);
        workloadSubjectAreaSearchChoiceBox.setValue("None");

        Workload workload = new Workload(appController.getLoginUser());
        UserWorkloadAllocation workloadUser = new UserWorkloadAllocation(appController.getLoginUser());
        appController.setWorkload(workload);
        appController.setWorkloadUser(workloadUser);
        createUserWorkloadTable(workload.getUserWorkloadAllocation());
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    protected void onClickImportWorkloadButton() {
        try {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(appController.getStage());
            if (selectedFile == null) {
                return;
            }
            System.out.println(selectedFile.getPath());
            appController.getWorkload().importActivities(selectedFile.getPath());
            importSuccessDialog();
        } catch (Exception e) {
            e.printStackTrace();
            importErrorDialog();
        }
    }

    @FXML
    protected void onClickSearchButton() {
        String staffName = null;
        Integer staffId = null;
        String subjectArea = workloadSubjectAreaSearchChoiceBox.getValue() == null ? null : workloadSubjectAreaSearchChoiceBox.getValue().toString();
        List<UserWorkloadAllocation> list = appController.getWorkload().getUserWorkloadAllocation();
        try {
            staffId = Integer.parseInt(workloadStaffSearchTextField.getText().trim());
        } catch (Exception e) {
            staffName = workloadStaffSearchTextField.getText().trim();
        }

        if (subjectArea != null && !subjectArea.equals("None")) {
            list = list.stream().filter(user -> user.getSubjectArea().equals(subjectArea)).toList();
        }

        if (staffId != null) {
            Integer finalStaffId = staffId;
            list = list.stream().filter(user -> user.getUserId() == finalStaffId).toList();
        } else if (staffName != null && !staffName.isBlank()) {
            String finalStaffName = staffName;
            list = list.stream().filter(user -> user.getName().toLowerCase().contains(finalStaffName)).toList();
        }
        createUserWorkloadTable(list);
    }

    private void createUserWorkloadTable(List<UserWorkloadAllocation> userWorkloadAllocations) {
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

        // remove all columns in TableView
        workloadTableView.getColumns().clear();

        // add columns to TableView
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
        list.addAll(userWorkloadAllocations);
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

    private void importErrorDialog() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // block interaction with the main window
        popupStage.initOwner(appController.getStage());

        Label message = new Label("Cannot import workload information.\nPlease try again.");
        Button closeButton = new Button("Close");

        closeButton.setOnAction(e -> popupStage.close());

        HBox buttonLayout = new HBox(10, closeButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        VBox popupLayout = new VBox(20, message, buttonLayout);
        popupLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        Scene popupScene = new Scene(popupLayout, 300, 150);
        popupStage.setTitle("Error Dialog");
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    private void importSuccessDialog() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // block interaction with the main window
        popupStage.initOwner(appController.getStage());

        Label message = new Label("Successfully import workload information.");
        Button closeButton = new Button("Close");

        closeButton.setOnAction(e -> {
            popupStage.close();
            try {
                appController.loadScene("workload.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        HBox buttonLayout = new HBox(10, closeButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        VBox popupLayout = new VBox(20, message, buttonLayout);
        popupLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        Scene popupScene = new Scene(popupLayout, 300, 150);
        popupStage.setTitle("Success Dialog");
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
