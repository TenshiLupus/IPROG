package com.example.clients;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.*;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.Socket;

//Javafx application for sending an image to another client. Opened in intellij
public class Client extends Application {

    //Global variables
    private Socket socket;
    private OutputStream os;
    private InputStream is;

    //Launches the javafx program when running the main thread
    public static void main(String[] args) {
        launch(args);
    }

    //Configure the window scene
    @Override
    public void start(Stage stage) {

        //assings a title to the window
        stage.setTitle("File Chooser Sample");

        //Esatblish a connection to the server that will pass over te data to the other clients
        try{
            this.socket = new Socket("localhost", 5000);
            this.is = socket.getInputStream();
            this.os = socket.getOutputStream();
        } catch (IOException e) {
            closeEverything(socket);
        }

        //Utilize the file choooser to 
        final FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Send a Picture");
        final ImageView imageContainer = new ImageView();

        //Configure the logic for the button that will be used to select an image file
        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {

                        //obtain a file from the filechooser
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            try {
                                //Convert file into a byte array

                                FileInputStream fis = new FileInputStream(file.getAbsolutePath());

                                DataOutputStream dos = new DataOutputStream(os);

                                byte[] fileContent = new byte[(int)file.length()];
                                fis.read(fileContent);

                                dos.writeInt(fileContent.length);
                                dos.write(fileContent);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });

        //Instantiate a client thread alongside the application
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Listening thread is running");
                    while (!socket.isClosed()) {

                        DataInputStream dis = new DataInputStream(is);

                        //read the length of the file sent over from the client
                        int fileLength = dis.readInt();
                        if(fileLength > 0) {
                            byte[] fileContent = new byte[fileLength];
                            dis.readFully(fileContent, 0, fileLength);

                            File receivedImage = new File("x.jpg");
                            FileOutputStream fos = new FileOutputStream(receivedImage);
                            fos.write(fileContent);
                            fos.close();

                            //Convert the data sotre the in the retrieved image file to an image that is set to the imageContainer
                            Image imageToDisplay = new Image(new FileInputStream(receivedImage));

                            imageContainer.setImage(imageToDisplay);
                            imageContainer.setPreserveRatio(true);
                        }

                        System.out.println("image should be displaying");

                    }
                    System.out.println("Listening thread has ended");
                } catch(IOException ex){
                    System.out.println("Error ocurred");
                    ex.printStackTrace();
                }
            }
        });
        clientThread.start();

        //Aesthethic configureations for the window view
        final FlowPane fPane = new FlowPane();

        fPane.setHgap(6);
        fPane.setVgap(6);
        fPane.setOrientation(Orientation.VERTICAL);
        fPane.setAlignment(Pos.CENTER);
        fPane.getChildren().addAll(openButton, imageContainer);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(fPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup,800, 800));
        stage.show();
        System.out.println("Window should have opened");
    }


    //function taken from a stackoverflow post that converts a java.awt image to an FX image due to compatibility issues
    private Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }

    //Closes the socket resource
    public void closeEverything(Socket socket){
        try{
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
