package com.uon.uonwams.controllers;

import com.uon.uonwams.WAMSApplicationViewer;
import com.uon.uonwams.config.State;
import com.uon.uonwams.models.User;
import com.uon.uonwams.models.UserWorkloadAllocation;
import com.uon.uonwams.models.WAMSApplication;
import com.uon.uonwams.models.Workload;
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
    private User loginUser = null;
    private UserWorkloadAllocation workloadUser = null;
    private Workload workload = null;
    private State state = null;

    public AppController(Stage stage) throws Exception {
        new WAMSApplication();
        this.state = State.LOGIN;
        this.stage = stage;
        stage.setTitle("Workload Allocation System");
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Stage getStage() {
        return stage;
    }

    public void loadScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WAMSApplicationViewer.class.getResource(fxml));
        Parent root = fxmlLoader.load();
        ControllerInterface controller = fxmlLoader.getController();
        if (controller != null) {
            controller.setAppController(this);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        controller.setup();
    }

    public <T> void createActionButton(TableColumn<T, String> column, String buttonName, ClickButtonCallback callback) {
        Callback<TableColumn<T, String>, TableCell<T, String>> cellFactory = new Callback<>() {
            @Override
            public TableCell call(final TableColumn<T, String> param) {
                final TableCell<T, String> cell = new TableCell<T, String>() {

                    final Button button = new Button(buttonName);

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            button.setOnAction(event -> {
                                T data = getTableView().getItems().get(getIndex());
                                try {
                                    callback.execute(data, AppController.this);
                                } catch (IOException e) {
                                    System.out.println(e);
                                    throw new RuntimeException(e);
                                }
                            });
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


    public <T> void createEditDeleteButtons(TableColumn<T, String> column, ClickButtonCallback editButtonCallback, ClickButtonCallback deleteButtonCallback) {
        Callback<TableColumn<T, String>, TableCell<T, String>> cellFactory = new Callback<>() {
            @Override
            public TableCell call(final TableColumn<T, String> param) {
                final TableCell<T, String> cell = new TableCell<T, String>() {

                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    private final HBox hbox = new HBox(5, editButton, deleteButton);

                    {
                        editButton.setOnAction(event -> {
                            T data = getTableView().getItems().get(getIndex());
                            try {
                                editButtonCallback.execute(data, AppController.this);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        deleteButton.setOnAction(event -> {
                            T data = getTableView().getItems().get(getIndex());
                            try {
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
