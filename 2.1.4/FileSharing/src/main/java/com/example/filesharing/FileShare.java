package com.example.filesharing;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import java.util.Scanner;

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
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.text.html.ImageView;

public class FileShare extends Application {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 800;

    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String userName;
    private Desktop desktop = Desktop.getDesktop();


    public void start(Stage stage) {
        //create a Scene
        stage.setTitle("File Chooser Sample");


        try{
            Scanner sc = new Scanner(System.in);
            socket = new Socket("localhost", 5000);
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
            System.out.println("Enter your display name");
            userName = sc.nextLine();

        } catch (IOException e) {
            closeEverything(socket, br, bw);
        }

        final FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Open a Picture...");

        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        final FileChooser fileChooser = new FileChooser();
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

        stage.setScene(new Scene(rootGroup,WIDTH, HEIGHT));
        stage.show();
        System.out.println("Window should have opened");
        listenForMessage();
        sendMessage();
    }

    public void sendMessage(){
        try{
            bw.write(userName);
            bw.newLine();
            bw.flush();



        } catch (IOException e) {
            closeEverything(socket, br, bw);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while(socket.isConnected()){
                    try{
                        msgFromGroupChat = br.readLine();
                        System.out.println(msgFromGroupChat);
                    }catch (IOException ioe){
                        closeEverything(socket, br, bw);
                    }
                }
            }
        }).start();
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

    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }

    private void openFile(File file) throws IOException{
            desktop.open(file);
    }
}