package com.uon.uonwams;

import com.uon.uonwams.controllers.AppController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class WAMSApplicationViewer extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // initialize Java FX application
            new AppController(stage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}