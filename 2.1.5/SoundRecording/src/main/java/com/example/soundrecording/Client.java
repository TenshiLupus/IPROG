package com.example.soundrecording;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import javax.sound.sampled.*;

import java.io.*;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;


//Client that will be utilized to record and and send the audio to another client
public class Client extends Application {

    private Socket socket;
    private OutputStream os;
    private InputStream is;

    private File receivedRecording;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {


//create a Scene
        stage.setTitle("File Chooser Sample");

        //Initialize the client socket
        try{
            this.socket = new Socket("localhost", 5000);
            this.is = socket.getInputStream();
            this.os = socket.getOutputStream();
            this.receivedRecording = null;
        } catch (IOException e) {
            //Close all the resources that won't be utilized
            closeEverything(socket);
        }

        //Setup UI buttons
        final Button recordButton = new Button("Record Audio");
        final Button playButton = new Button("Listen to received recording");

        try {

            //Setup audio recording components.
            //Setup audio sampling, bitrate and channels
            AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

            DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            if (!AudioSystem.isLineSupported(dataInfo)) {
                System.out.println("Not Supported");
            }
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            targetLine.open();

            
        //Utilize the audio recording line to output the data into a file till the user prommpts to stop
            recordButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {

                    System.out.println("event activated");
                    targetLine.start();

                    File outputFile = new File("recording.wav");
                    Thread audioRecordingThread = new Thread(() -> {
                        AudioInputStream recordingStream = new AudioInputStream(targetLine);
                        try {
                            AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                        }catch (IOException ex) {
                            System.out.println(ex);
                        }
                    });
                    audioRecordingThread.start();

                    //If the user presses on the ok button, this thread continues and stops the audio recording thread
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Stop recording?");
                    alert.showAndWait();
                    targetLine.stop();
                    targetLine.close();

                    System.out.println("Recording has closed");
                    DataOutputStream dos = new DataOutputStream(os);

                    //Creates a stream for reading the content of the generated recording.
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(outputFile);

                        //create a small buffer for the length of hte file
                        byte[] fileContent = new byte[(int) outputFile.length()];
                        fis.read(fileContent);

                        //notify the server socket the length to read on receival
                        dos.writeInt(fileContent.length);
                        dos.write(fileContent);

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    System.out.println("Image has been sent out");
                }
            });

            //Client thread that will be utilized to received incoming data from the other side of the client socket
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
                                System.out.println("New recording received");

                                byte[] fileContent = new byte[fileLength];
                                dis.readFully(fileContent, 0, fileLength);

                                receivedRecording = new File("receivedRecording.wav");
                                FileOutputStream fos = new FileOutputStream(receivedRecording);

                                //Insert the received content bytes into the file
                                fos.write(fileContent);
                                fos.close();

                            }
                            System.out.println("File has been written");
                        }
                        System.out.println("Listening thread has ended");
                    } catch(IOException ex){
                        System.out.println("Error ocurred");
                        ex.printStackTrace();
                    }
                }
            });
            clientThread.start();
            
            //Plays the received clip utilizing the audio system
            playButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {

                    try{
                        AudioInputStream audio = AudioSystem.getAudioInputStream(receivedRecording);
                        Clip audioClip = AudioSystem.getClip();
                        audioClip.open(audio);
                        audioClip.start();

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });

        }catch (Exception exception){
            exception.printStackTrace();
        }


        //Aesthetic configuratio of the scene window
        final FlowPane fPane = new FlowPane();

        fPane.setHgap(6);
        fPane.setVgap(6);
        fPane.setOrientation(Orientation.VERTICAL);
        fPane.setAlignment(Pos.CENTER);
        fPane.getChildren().addAll(recordButton, playButton);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(fPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup,800, 800));
        stage.show();
        System.out.println("Window should have opened");
    }

    //Close all teh utilized resources
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