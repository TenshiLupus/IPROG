
/**
 * @author Angel Cardenas Martinez anca8079
 */
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.Optional;
import java.util.ArrayList;

public class Testing extends Application {
    private static final int WIDTH = 400;
    private static final int HEIGTH = 600;
    private TextArea display;

    private TextField textField;
    private MenuButton menuButton = new MenuButton("Välj");
    private RadioButton name;
    private RadioButton value;


    public void start(Stage primaryStage) {
        //create a Scene
        BorderPane window = new BorderPane();
        primaryStage.setTitle("Sakregister");

        //center
        setCenterDisplay(window);

        setBottomButtons(window);
        Scene scene = new Scene(window, WIDTH, HEIGTH);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setCenterDisplay(BorderPane window) {
        display = new TextArea();
        window.setCenter(display);
        display.setEditable(false);
    }

    private void setBottomButtons(BorderPane window) {

        FlowPane bottom = new FlowPane();
        window.setBottom(bottom);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(10, 10, 10, 10));
        bottom.setHgap(5);
    }

    public static void main(String[] args) {

        launch(args);
    }


}