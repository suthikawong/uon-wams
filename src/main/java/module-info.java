module com.uon.uonwams {
    requires javafx.controls;
    requires javafx.fxml;
    requires password4j;
    requires com.opencsv;
    requires j.text.utils;
    requires java.mail;
    requires io.github.cdimascio.dotenv.java;


    opens com.uon.uonwams to javafx.fxml;
    exports com.uon.uonwams;
    exports com.uon.uonwams.controllers;
    opens com.uon.uonwams.controllers to javafx.fxml;
}