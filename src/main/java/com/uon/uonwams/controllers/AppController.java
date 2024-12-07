package com.uon.uonwams.controllers;

import com.uon.uonwams.WAMSApplicationViewer;
import com.uon.uonwams.config.State;
import com.uon.uonwams.models.User;
import com.uon.uonwams.models.WAMSApplication;
import com.uon.uonwams.models.Workload;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppController {
    private Stage stage;
    private User loginUser = null;
    private User workloadUser = null;
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

    public User getWorkloadUser() {
        return workloadUser;
    }

    public void setWorkloadUser(User workloadUser) {
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

    public void loadScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WAMSApplicationViewer.class.getResource(fxml));
        Parent root = fxmlLoader.load();
        BaseController controller = fxmlLoader.getController();
        if (controller != null) {
            controller.setAppController(this);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
