package com.uon.uonwams.controllers;

import com.uon.uonwams.data.Data;
import com.uon.uonwams.models.Activity;
import com.uon.uonwams.models.User;
import com.uon.uonwams.models.UserManagement;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class UserViewController extends MenuController implements ControllerInterface {
    AppController appController;

    @FXML
    private TableView userTableView;

    public void setup() {
        // add menu controller to manage page permission
        this.setMenuAppController(appController);
        // initialize user management instance with logged-in user
        UserManagement um = new UserManagement(appController.getLoginUser());
        appController.setUserManagement(um);
        // display user table
        createUserTable();
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    // when add button is clicked, navigate to the user-form.fxml
    @FXML
    protected void onClickAddButton() throws IOException {
        // reset user to ensure that the form is in adding state
        appController.setSelectedUser(null);
        appController.loadScene("user-form.fxml");
    }

    // display users in the table
    private void createUserTable() {
        TableColumn<User, String> userIdColumn = new TableColumn<>("User ID");
        userIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getUserId())));

        TableColumn<User, String> userNameColumn = new TableColumn<>("Name");
        userNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<User, String> fteRatioColumn = new TableColumn<>("FTE Ratio");
        fteRatioColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Double.toString(data.getValue().getFteRatio())));

        TableColumn<User, String> subjectAreaColumn = new TableColumn<>("Subject Area");
        subjectAreaColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSubjectArea()));

        TableColumn<User, String> lineManagerUserIdColumn = new TableColumn<>("Line Manager ID");
        lineManagerUserIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLineManagerUserId() == null ? "-" : Integer.toString(data.getValue().getLineManagerUserId())));

        TableColumn<User, String> lineManagerNameColumn = new TableColumn<>("Line Manager Name");
        lineManagerNameColumn.setCellValueFactory(data -> {
            if (data.getValue().getLineManagerUserId() == null) {
                // display "-" when lineManagerUserId is null
                return new javafx.beans.property.SimpleStringProperty("-");
            } else {
                User lineManager = null;
                // find the user instance of line managers
                for (User user: Data.userData.getUsers()) {
                    if (user.getUserId() == data.getValue().getLineManagerUserId()) {
                        lineManager = user;
                    }
                }
                // display the name of the line manager if exists
                return lineManager == null ? new javafx.beans.property.SimpleStringProperty("-") : new javafx.beans.property.SimpleStringProperty(lineManager.getName());
            }
        });

        TableColumn<User, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("dummy"));
        // create edit and delete buttons in the table
        appController.createEditDeleteButtons(actionColumn, UserViewController::handleClickEditButton, UserViewController::handleClickDeleteButton);

        // add columns to TableView
        userTableView.getColumns().addAll(
                userIdColumn,
                userNameColumn,
                emailColumn,
                fteRatioColumn,
                subjectAreaColumn,
                lineManagerUserIdColumn,
                lineManagerNameColumn,
                actionColumn
        );

        // find subordinates of the logged-in user
        List<User> users = appController.getUserManagement().getUsers();
        ObservableList<User> list = FXCollections.observableArrayList();
        for(User user: users) {
            list.add(user);
        }
        // add data to TableView
        userTableView.setItems(list);
    }

    // when edit button is clicked, set it as a selected user and navigate to the user-form.fxml
    private static <T> void handleClickEditButton(T data, AppController appController) throws IOException {
        if (User.class.isInstance(data)) {
            User user = User.class.cast(data);
            appController.setSelectedUser(user);
            appController.loadScene("user-form.fxml");
        }
    }

    // when delete button is clicked, display the dialog
    private static <T> void handleClickDeleteButton(T data, AppController appController) throws IOException {
        if (User.class.isInstance(data)) {
            User user = User.class.cast(data);
            // if this user is currently used as a line manager or there are activities that refer to it, deleting this user is not allowed
            boolean isUsed = checkUserIsUsed(user);
            if (isUsed) {
                unableDeleteDialog(appController);
            } else {
                confirmDeleteDialog(user, appController);
            }
        }
    }

    // show not allow deleting user dialog
    private static void unableDeleteDialog(AppController appController) {
        // create dialog
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // block interaction with the main window
        popupStage.initOwner(appController.getStage());

        Label message = new Label("This user have been used. Delete is not allowed.");
        Button cancelButton = new Button("Close");

        // attach event listener when buttons are clicked
        // click cancel: close dialog
        cancelButton.setOnAction(e -> popupStage.close());

        // dialog layout
        HBox buttonLayout = new HBox(10, cancelButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        VBox popupLayout = new VBox(20, message, buttonLayout);
        popupLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        Scene popupScene = new Scene(popupLayout, 300, 150);

        // set scene
        popupStage.setTitle("Unable Delete");
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    // show confirm deleting dialog
    private static void confirmDeleteDialog(User user, AppController appController) {
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
        deleteButton.setOnAction(e -> {
            // delete user
            appController.getUserManagement().deleteUser(user.getUserId());
            // close dialog
            popupStage.close();
            try {
                // navigate to user-view.fxml
                appController.loadScene("user-view.fxml");
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

    // check is user have been used
    private static boolean checkUserIsUsed(User user) {
        // check is user has activities
        for (Activity activity: Data.activityData.getActivities()) {
            if (activity.getResponsibleUserId() == user.getUserId()) {
                return true;
            }
        }
        // check is user a line manager
        if (user.checkIsLineManager()) {
            return true;
        }
        return false;
    }
}
