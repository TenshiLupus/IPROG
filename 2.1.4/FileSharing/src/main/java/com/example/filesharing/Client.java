package com.example.filesharing;

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

import java.awt.*;
import java.io.File;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Application {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 800;

    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String userName;

    private Desktop desktop = Desktop.getDesktop();

    public Client(Socket socket, String userName,String[] args){
        try{
            this.socket = socket;
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
            Application.launch(args);
        } catch (IOException e) {
            closeEverything(socket, br, bw);
        }

    }

    //Send message over to the client handler for printing to other clients
    //Where image will need to be sent from
    public void sendMessage(){
        try{
            bw.write(userName);
            bw.newLine();
            bw.flush();

            Scanner sc = new Scanner(System.in);
            while(socket.isConnected()){
                String message = sc.nextLine();
                bw.write(userName + ": " + message);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, br, bw);
        }
    }


    // Listen for incoming messaages from the server
    // Here is where the image needs to be retrieved and appended to a window.
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

    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your display name");
        String username = sc.nextLine();
        Socket socket = new Socket("localhost", 5000);
        Client c = new Client(socket, username, args);
        c.listenForMessage();
        c.sendMessage();
    }

    @Override
    public void start(Stage stage) throws Exception {

    }


}
