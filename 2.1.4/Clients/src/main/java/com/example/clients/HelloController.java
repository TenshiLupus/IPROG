package com.example.clients;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class HelloController implements Initializable {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String userName;
    private Desktop desktop = Desktop.getDesktop();
    @FXML
    private ImageView image_container;
    @FXML
    private Button send_button;
    @FXML
    private Button listen_button;

    @FXML
    protected void startListening() {
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

    private void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw){
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Stage st = (Stage) image_container.getScene().getWindow();

        send_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                final FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(st);
                if (file != null) {
                    try {
                        openFile(file);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        });

        listen_button.setOnAction();
    }
}