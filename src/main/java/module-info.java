module com.uon.uonwams {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.uon.uonwams to javafx.fxml;
    exports com.uon.uonwams;
}