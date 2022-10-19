package com.example.emailsender;
/**
 * @author Angel Cardenas Martinez anca8079
 */
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Testing extends Application {
    private static final int WIDTH = 900;
    private static final int HEIGTH = 800;

    private ArrayList<TextField> serverInputs = new ArrayList<>();
    private ArrayList<Control> messageInputs = new ArrayList();


    public void start(Stage primaryStage) {
        //create a Scene
        primaryStage.setTitle("EmailSender");

        FlowPane sp = new FlowPane();
        sp.setOrientation(Orientation.VERTICAL);

        //Server Inputs
        TextField serverName = new TextField();
        serverName.setPromptText("SERVER NAME");
        messageInputs.add(serverName);
        TextField userName = new TextField();
        userName.setPromptText("USER NAME");
        messageInputs.add(userName);
        TextField password = new TextField();
        password.setPromptText("PASSWORD");
        messageInputs.add(password);

        //Emails components
        TextField from = new TextField();
        from.setPromptText("FROM");
        messageInputs.add(from);
        TextField to = new TextField();
        to.setPromptText("TO");
        messageInputs.add(to);
        TextField subject = new TextField();
        subject.setPromptText("SUBJECT");
        messageInputs.add(subject);
        TextArea message = new TextArea();
        message.setPromptText("MESSAGE");
        messageInputs.add(message);

        Button send = new Button("SEND");

        send.setPadding(new Insets(10, 10,10,10));
        message.setMinHeight(50);

        sp.setVgap(30);
        sp.setAlignment(Pos.CENTER);
        sp.setPadding(new Insets(10, 4, 10, 4));
        sp.getChildren().addAll(serverName, userName, password, from, to, subject, message, send);

        EventHandler<ActionEvent> event = new EventHandler<>() {
            public void handle(ActionEvent e) {
                Properties properties = new Properties();

                properties.put("mail.smtp.auth", true);
                properties.put("mail.smtp.host", serverName.getText());
                properties.put("mail.smtp.port", 587);
                properties.put("mail.smtp.starttls.enable", true);
                properties.put("mail.transport.protocol", "smtp");

                Session session = Session.getInstance(properties, new Authenticator() {
                    /*
                    Using gmails 2fa and a generated password to request access to gmails service and send the email
                    previous method of sending an email has been deprecated since 30th of may 2022
                     */
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName.getText(), password.getText());
                    }
                });

                MimeMessage mimeMessage = new MimeMessage(session);
                try {
                    Address addressFrom = new InternetAddress(from.getText());
                    Address addressTo = new InternetAddress(to.getText());

                    mimeMessage.setFrom(addressFrom);
                    mimeMessage.setRecipient(Message.RecipientType.TO, addressTo);
                    mimeMessage.setSubject(subject.getText());
                    mimeMessage.setContent(message.getText(), "text/plain");

                    Transport.send(mimeMessage);
                    System.out.println("Email sent");
                } catch (MessagingException ex) {
                    throw new RuntimeException(ex);
                }
                finally {
                    for(Control field : messageInputs){
                        if(field instanceof TextField){
                            TextField tField = (TextField) field;
                            tField.setText("");
                        }
                        if(field instanceof TextArea){
                            TextArea taField = (TextArea) field;
                            taField.setText("");
                        }
                    }

                }
            }
        };

        send.setOnAction(event);

        Scene scene = new Scene(sp, WIDTH, HEIGTH);
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    public static void main(String[] args) {

        launch(args);
    }


}