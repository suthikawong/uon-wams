package com.uon.uonwams;

import com.uon.uonwams.controllers.AppController;
import com.uon.uonwams.controllers.BaseController;
import com.uon.uonwams.models.WAMSApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class WAMSApplicationViewer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            new AppController(stage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
            return;
//            throw e;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}