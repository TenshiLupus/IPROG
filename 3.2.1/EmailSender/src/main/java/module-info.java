module com.example.emailsender {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.emailsender to javafx.fxml;
    exports com.example.emailsender;
}