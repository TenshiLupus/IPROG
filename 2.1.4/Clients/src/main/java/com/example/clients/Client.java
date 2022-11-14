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


public class Client extends Application {

    private Socket socket;

    private OutputStream os;
    private InputStream is;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {


//create a Scene
        stage.setTitle("File Chooser Sample");
        try{
            this.socket = new Socket("localhost", 5000);

        } catch (IOException e) {
            closeEverything(socket);
        }

        final FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Send a Picture");
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

                                BufferedImage fileImage = ImageIO.read(new FileInputStream(file.getAbsolutePath()));
                                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

                                BufferedImage bufferedImage = new BufferedImage(fileImage.getWidth(), fileImage.getHeight(), BufferedImage.TYPE_INT_RGB);

                                Graphics graphics = bufferedImage.createGraphics();
                                graphics.drawImage(fileImage, 0,0,null);
                                graphics.dispose();

                                ImageIO.write(bufferedImage, "jpg", bos);
                                bos.close();

                                System.out.println("image Sent out");

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });

        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Listening thread is running");
                    while (!socket.isClosed()) {

                        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());

                        if(bis != null) {
                            BufferedImage bi = ImageIO.read(bis);
                            bis.close();

                            Image imageToDisplay = convertToFxImage(bi);

                            imageContainer.setImage(imageToDisplay);
                            imageContainer.setPreserveRatio(true);

                            System.out.println("image should be displaying");
                        }
                    }
                    System.out.println("Listening thread has ended");
                } catch(IOException ex){
                    System.out.println("Error ocurred");
                    throw new RuntimeException(ex);

                }
            }
        });
        clientThread.start();

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


    //function taken from a stackoverflow post
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
