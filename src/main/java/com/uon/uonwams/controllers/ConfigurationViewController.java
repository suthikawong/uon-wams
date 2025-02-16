/**
 Program: UON WAMS Application
 Filename: ConfigurationViewController.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.controllers;

import com.uon.uonwams.data.Data;
import com.uon.uonwams.models.ActivityType;
import io.github.cdimascio.dotenv.Dotenv;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

public class ConfigurationViewController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private TableView configurationTableView;

    @FXML
    private Label configurationFormTotalFullTimeHoursLabel;

    public void setup() {
        // add menu controller to manage page permission
        this.setMenuAppController(appController);

        // set total full time hours value
        configurationFormTotalFullTimeHoursLabel.setText(Integer.toString(Data.configurationData.getTotalFullTimeHours()));
        // display activity type table
        createActivityTypeTable();
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    // import activity types from the selected CSV file
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

    // download CSV template
    @FXML
    protected void onClickDownloadTemplateButton() {
        Dotenv dotenv = Dotenv.load();
        // get CSV template file path
        String activityTypeTemplateFilePath = dotenv.get("ACTIVITY_TYPE_TEMPLATE_FILE_PATH");

        File existingFile = new File(activityTypeTemplateFilePath);
        // if there is no CSV file in that file path, log in the monitor and exits the function
        if (!existingFile.exists()) {
            System.out.println("File not found: " + existingFile.getAbsolutePath());
            return;
        }

        // initialize FileChooser to save the file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        // set a default name for the CSV file
        fileChooser.setInitialFileName("activity-type-template.csv");

        // show save dialog
        File targetFile = fileChooser.showSaveDialog(appController.getStage());
        // if file is selected
        if (targetFile != null) {
            try (FileInputStream inputStream = new FileInputStream(existingFile);
                 FileOutputStream outputStream = new FileOutputStream(targetFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;

                // copy the file
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // when edit button is clicked, navigate to the configuration-form.fxml
    @FXML
    protected void onClickEditConfigurationButton() throws IOException {
        appController.loadScene("configuration-form.fxml");
    }

    // display activity types in the table
    private void createActivityTypeTable() {
        TableColumn<ActivityType, String> activityTypeIdColumn = new TableColumn<>("ID");
        activityTypeIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getId())));
        // add columns to TableView
        configurationTableView.getColumns().add(activityTypeIdColumn);

        TableColumn<ActivityType, String> activityTypeColumn = new TableColumn<>("Activity Type");
        activityTypeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        // add columns to TableView
        configurationTableView.getColumns().add(activityTypeColumn);

        if (Data.configurationData.getActivityTypes().size() > 0) {
            LinkedHashMap<String, Double> formula = Data.configurationData.getActivityTypes().getFirst().getFormula();
            for(String key : formula.keySet()) {
                TableColumn<ActivityType, String> column = new TableColumn<>(key);
                column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Double.toString(data.getValue().getFormula().get(key))));
                // add columns to TableView
                configurationTableView.getColumns().add(column);
            }
        }

        // find activity types
        ObservableList<ActivityType> list = FXCollections.observableArrayList();
        list.addAll(Data.configurationData.getActivityTypes());
        // add data to TableView
        configurationTableView.setItems(list);
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
                appController.loadScene("configuration-view.fxml");
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
