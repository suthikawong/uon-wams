/**
 Program: UON WAMS Application
 Filename: AppController.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.controllers;

import com.uon.uonwams.WAMSApplicationViewer;
import com.uon.uonwams.data.Data;
import com.uon.uonwams.models.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

@FunctionalInterface
interface ClickButtonCallback {
    <T> void execute(T data, AppController appController) throws IOException;
}

public class AppController {
    private Stage stage;
    // the below variables are used to store value that will be pass between different pages
    private User loginUser = null;
    private UserWorkloadAllocation workloadUser = null;
    private Workload workload = null;
    private UserManagement userManagement = null;
    private Activity selectedActivity = null;
    private User selectedUser = null;

    public AppController(Stage stage) throws Exception {
        // initialize data from DAT file
        new Data();
        this.stage = stage;
        // set scene
        stage.setTitle("Workload Allocation System");
        stage.setResizable(false);
        this.loadScene("login.fxml");
    }

    public User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public UserWorkloadAllocation getWorkloadUser() {
        return workloadUser;
    }

    public void setWorkloadUser(UserWorkloadAllocation workloadUser) {
        this.workloadUser = workloadUser;
    }

    public Workload getWorkload() {
        return workload;
    }

    public void setWorkload(Workload workload) {
        this.workload = workload;
    }

    public UserManagement getUserManagement() {
        return userManagement;
    }

    public void setUserManagement(UserManagement userManagement) {
        this.userManagement = userManagement;
    }

    public Stage getStage() {
        return stage;
    }

    public Activity getSelectedActivity() {
        return selectedActivity;
    }

    public void setSelectedActivity(Activity selectedActivity) {
        this.selectedActivity = selectedActivity;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    // load and setup scene
    public void loadScene(String fxml) throws IOException {
        // load fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(WAMSApplicationViewer.class.getResource(fxml));
        Parent root = fxmlLoader.load();
        ControllerInterface controller = fxmlLoader.getController();
        if (controller != null) {
            // pass appController to every page
            controller.setAppController(this);
        }
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.show();

        // call setup() method
        // the purpose of this is to ensure that code inside setup() will be executed after appController was passed
        controller.setup();
    }

    // create a button in the table
    public <T> void createActionButton(TableColumn<T, String> column, String buttonName, ClickButtonCallback callback) {
        Callback<TableColumn<T, String>, TableCell<T, String>> cellFactory = new Callback<>() {

            // override cell that will be created in the table
            @Override
            public TableCell call(final TableColumn<T, String> param) {
                final TableCell<T, String> cell = new TableCell<T, String>() {

                    // create button
                    final Button button = new Button(buttonName);

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            button.setOnAction(event -> {
                                // get data
                                T data = getTableView().getItems().get(getIndex());
                                try {
                                    // pass data and execute callback function
                                    callback.execute(data, AppController.this);
                                } catch (IOException e) {
                                    System.out.println(e);
                                    throw new RuntimeException(e);
                                }
                            });
                            // set created button in the table
                            setGraphic(button);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        column.setCellFactory(cellFactory);
    }

    // create edit and delete buttons in the table
    public <T> void createEditDeleteButtons(TableColumn<T, String> column, ClickButtonCallback editButtonCallback, ClickButtonCallback deleteButtonCallback) {
        Callback<TableColumn<T, String>, TableCell<T, String>> cellFactory = new Callback<>() {

            // override cell that will be created in the table
            @Override
            public TableCell call(final TableColumn<T, String> param) {
                final TableCell<T, String> cell = new TableCell<T, String>() {

                    // create buttons
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    private final HBox hbox = new HBox(5, editButton, deleteButton);

                    {
                        editButton.setOnAction(event -> {
                            // get data
                            T data = getTableView().getItems().get(getIndex());
                            try {
                                // pass data and execute callback function
                                editButtonCallback.execute(data, AppController.this);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        deleteButton.setOnAction(event -> {
                            // get data
                            T data = getTableView().getItems().get(getIndex());
                            try {
                                // pass data and execute callback function
                                deleteButtonCallback.execute(data, AppController.this);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // set created buttons in the table
                            setGraphic(hbox);
                        }
                    }
                };
                return cell;
            }
        };
        column.setCellFactory(cellFactory);
    }
}
