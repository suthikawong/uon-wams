package com.uon.uonwams.controllers;

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
        this.setMenuAppController(appController);
        UserManagement um = new UserManagement(appController.getLoginUser());
        appController.setUserManagement(um);
        createUserTable();
    }

    @Override
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    protected void onClickAddButton() throws IOException {
        appController.setSelectedUser(null);
        appController.loadScene("user-form.fxml");
    }

    private void createUserTable() {
        TableColumn<User, String> userIdColumn = new TableColumn<>("User ID");
        userIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getUserId())));

        TableColumn<User, String> userNameColumn = new TableColumn<>("Name");
        userNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<User, String> fteRatioColumn = new TableColumn<>("FTE Ratio");
        fteRatioColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Float.toString(data.getValue().getFteRatio())));

        TableColumn<User, String> subjectAreaColumn = new TableColumn<>("Subject Area");
        subjectAreaColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSubjectArea()));

        TableColumn<User, String> lineManagerUserIdColumn = new TableColumn<>("Line Manager ID");
        lineManagerUserIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getLineManagerUserId())));

        TableColumn<User, String> lineManagerNameColumn = new TableColumn<>("Line Manager Name");
        lineManagerNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(appController.getLoginUser().getName()));

        TableColumn<User, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("dummy"));
        appController.createEditDeleteButtons(actionColumn, UserViewController::handleClickEditButton, UserViewController::handleClickDeleteButton);

        // Add Columns to TableView
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

        List<User> users = appController.getUserManagement().getUsers();
        ObservableList<User> list = FXCollections.observableArrayList();
        for(User user: users) {
            list.add(user);
        }
        userTableView.setItems(list);
    }

    private static <T> void handleClickEditButton(T data, AppController appController) throws IOException {
        if (User.class.isInstance(data)) {
            User user = User.class.cast(data);
            appController.setSelectedUser(user);
            appController.loadScene("user-form.fxml");
        }
    }

    private static <T> void handleClickDeleteButton(T data, AppController appController) throws IOException {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // block interaction with the main window
        popupStage.initOwner(appController.getStage());

        Label message = new Label("Are you sure you want to delete this item?");
        Button cancelButton = new Button("Cancel");
        Button deleteButton = new Button("Delete");

        cancelButton.setOnAction(e -> popupStage.close());
        deleteButton.setOnAction(e -> {
            deleteUser(data, appController);
            popupStage.close();
            // reload users
            try {
                appController.loadScene("user-view.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        HBox buttonLayout = new HBox(10, cancelButton, deleteButton);
        buttonLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        VBox popupLayout = new VBox(20, message, buttonLayout);
        popupLayout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 10;");
        Scene popupScene = new Scene(popupLayout, 300, 150);
        popupStage.setTitle("Confirm Delete");
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    private static <T> void deleteUser(T data, AppController appController) {
        if (User.class.isInstance(data)) {
            User user = User.class.cast(data);
            appController.getUserManagement().deleteUser(user.getUserId());
        }
    }

}
