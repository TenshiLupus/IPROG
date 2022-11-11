package com.example.clients;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private String clientUsername;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.is = socket.getInputStream();
            this.os = socket.getOutputStream();

            clientHandlers.add(this);
            broadcastMessage("SERVER " + clientUsername + "has entered");
        }catch (IOException e){
            closeEverything(socket, bufferr, bufferw);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try{
                messageFromClient = bufferr.readLine();
                broadcastMessage(messageFromClient);
            }catch(IOException e){
                closeEverything(socket, bufferr, bufferw);
                break;
            }

        }
    }

    //Sends a message to all connected users in the clienthandlersList
    public void broadcastMessage(String message){

            for(ClientHandler clientHandler : clientHandlers){
                try{
                    if(!clientHandler.clientUsername.equals(clientUsername)){

                    }
                } catch (IOException e) {
                    closeEverything(socket, bufferr, bufferw);
                }
            }

    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("sERVER: " + clientUsername + "has left the chat");
    }

    public void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw){
        removeClientHandler();
        try{
            if(br != null){
                bufferr.close();
            }
            if(bw != null){
                bufferw.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
