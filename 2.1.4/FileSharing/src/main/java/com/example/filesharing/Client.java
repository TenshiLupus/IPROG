package com.example.filesharing;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String userName;

    public Client(Socket socket, String userName){
        try{
            this.socket = socket;
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
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

                bw.write(userName + ": ");
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
        System.out.println("Client has initiated");
        String username = sc.nextLine();
        Socket socket = new Socket("localhost", 5000);
        Client c = new Client(socket, username);
        c.listenForMessage();
        c.sendMessage();
    }
}
