/**
 Program: UON WAMS Application
 Filename: ActivityViewController.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.controllers;

import com.uon.uonwams.data.Data;
import com.uon.uonwams.models.ActivityType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class FormulaViewController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private TableView formulaTableView;

    public void setup() {
        // add menu controller to manage page permission
        this.setMenuAppController(appController);
        // display activity type table
        createActivityTypeTable();
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
    protected void onClickImportActivityTypesButton() {
        try {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(appController.getStage());

            // if no file is selected, exits the function
            if (selectedFile == null) {
                return;
            }
            // import activities data
            appController.getWorkload().importActivityTypes(selectedFile.getPath());
            // display import success dialog
            importSuccessDialog();
        } catch (Exception e) {
            e.printStackTrace();
            // display import fail dialog
            importErrorDialog(e.getMessage());
        }
    }

    // display activity types in the table
    private void createActivityTypeTable() {
        TableColumn<ActivityType, String> activityTypeIdColumn = new TableColumn<>("ID");
        activityTypeIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getId())));
        // add columns to TableView
        formulaTableView.getColumns().add(activityTypeIdColumn);

        TableColumn<ActivityType, String> activityTypeColumn = new TableColumn<>("Activity Type");
        activityTypeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        // add columns to TableView
        formulaTableView.getColumns().add(activityTypeColumn);

        if (Data.activityTypeData.getActivityTypes().size() > 0) {
            LinkedHashMap<String, Double> formula = Data.activityTypeData.getActivityTypes().getFirst().getFormula();
            for(String key : formula.keySet()) {
                TableColumn<ActivityType, String> column = new TableColumn<>(key);
                column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Double.toString(data.getValue().getFormula().get(key))));
                // add columns to TableView
                formulaTableView.getColumns().add(column);
            }
        }

        // find activity types
        ObservableList<ActivityType> list = FXCollections.observableArrayList();
        list.addAll(Data.activityTypeData.getActivityTypes());
        // add data to TableView
        formulaTableView.setItems(list);
    }

    // show import error dialog
    private void importErrorDialog(String errorMessage) {
        // create dialog
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // block interaction with the main window
        popupStage.initOwner(appController.getStage());

        Label message = new Label(errorMessage);
        Button closeButton = new Button("Close");

        // attach event listener when button is clicked
        // click close: close dialog
        closeButton.setOnAction(e -> popupStage.close());

        // dialog layout
        HBox buttonLayout = new HBox(10, closeButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        VBox popupLayout = new VBox(20, message, buttonLayout);
        popupLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        Scene popupScene = new Scene(popupLayout, 400, 150);

        // set scene
        popupStage.setTitle("Error Dialog");
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    // show import success dialog
    private void importSuccessDialog() {
        // create dialog
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // block interaction with the main window
        popupStage.initOwner(appController.getStage());
        // prevent closing by x
        popupStage.setOnCloseRequest((event) -> event.consume());

        Label message = new Label("Successfully import activity types.");
        Button closeButton = new Button("Close");

        // attach event listener when button is clicked
        // click close: close dialog
        closeButton.setOnAction(e -> {
            // close dialog
            popupStage.close();
            try {
                // navigate to workload.fxml
                appController.loadScene("formula-view.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // dialog layout
        HBox buttonLayout = new HBox(10, closeButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        VBox popupLayout = new VBox(20, message, buttonLayout);
        popupLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        Scene popupScene = new Scene(popupLayout, 300, 150);

        // set scene
        popupStage.setTitle("Success Dialog");
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
