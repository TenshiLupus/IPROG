module com.example.soundrecording {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.soundrecording to javafx.fxml;
    exports com.example.soundrecording;
}