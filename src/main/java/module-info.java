module com.uon.uonwams {
    requires javafx.controls;
    requires javafx.fxml;
    requires password4j;


    opens com.uon.uonwams to javafx.fxml;
    exports com.uon.uonwams;
    exports com.uon.uonwams.controllers;
    opens com.uon.uonwams.controllers to javafx.fxml;
}