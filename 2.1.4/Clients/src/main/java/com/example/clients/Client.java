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
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;


public class Client extends Application {
    private final Desktop desktop = Desktop.getDesktop();
    private Socket socket = null;
    private BufferedWriter bw = null;
    private BufferedReader br = null;

    private OutputStream os = null;
    private InputStream is = null;

    private int maxSize = 999999999;
    private int byteRead;
    private int current = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {


//create a Scene
        stage.setTitle("File Chooser Sample");
        try{
            socket = new Socket("localhost", 5000);
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
        } catch (IOException e) {
            closeEverything(socket, br, bw);
        }

        final FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Send a Picture");
        final Button listenButton = new Button("Start listening");
        final ImageView imageContainer = new ImageView();
        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {

                        //obtain a file from the filechooser
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            try {
                                //Convert file into a byte array
                                OutputStream os = socket.getOutputStream();
                                BufferedOutputStream bos = new BufferedOutputStream(os);
                                ImageIcon imagecon = new ImageIcon(file.getAbsolutePath());
                                Image image = imagecon.getImage();
                                BufferedImage bi = new BufferedImage((int)image.getWidth(null), (int)image.getHeight(null), BufferedImage.TYPE_INT_RGB);

                                Graphics graphics = bi.createGraphics();
                                graphics.drawImage(image, 0,0,null);
                                graphics.dispose();
                                ImageIO.write(bi, "jpg", bos);

                                bos.close();
                                socket.close();

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }

                        //Convert file into a byte array
                        byte[] buffer = new byte[(int) file.length()];
                    }
                });


        listenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                try {

                    is = socket.getInputStream();

                    File test = new File("x.jpg");
                    test.createNewFile();
                    FileOutputStream fos = new FileOutputStream(test);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);


                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        final FlowPane fPane = new FlowPane();

        fPane.setHgap(6);
        fPane.setVgap(6);
        fPane.setOrientation(Orientation.VERTICAL);
        fPane.setAlignment(Pos.CENTER);
        fPane.getChildren().addAll(openButton, listenButton);

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
