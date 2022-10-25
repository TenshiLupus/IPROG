module com.example.filesharing {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.desktop;


    opens com.example.filesharing to javafx.fxml;
    exports com.example.filesharing;
}