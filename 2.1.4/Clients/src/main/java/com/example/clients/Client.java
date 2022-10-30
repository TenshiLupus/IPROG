package com.example.clients;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.*;
import java.net.Socket;


public class Client extends Application {
    private final Desktop desktop = Desktop.getDesktop();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {


//create a Scene
        stage.setTitle("File Chooser Sample");

        Socket socket = null;
        BufferedWriter bw = null;
        BufferedReader br = null;

        try{

            socket = new Socket("localhost", 5000);
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            closeEverything(socket, br, bw);
        }

        final FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Open a Picture...");

        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {

                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            try {
                                openFile(file);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });


        final FlowPane fPane = new FlowPane();

        fPane.setHgap(6);
        fPane.setVgap(6);
        fPane.setOrientation(Orientation.VERTICAL);
        fPane.setAlignment(Pos.CENTER);
        fPane.getChildren().addAll(openButton);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(fPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup,800, 800));
        stage.show();
        System.out.println("Window should have opened");
    }

    public void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw){
        try{
            if(br != null){
                br.close();
            }
            if(bw != null){
                bw.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFile(File file) throws IOException{
        desktop.open(file);
    }
}
