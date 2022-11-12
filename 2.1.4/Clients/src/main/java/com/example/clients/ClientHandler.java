package com.example.clients;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private String clientUsername;

    private BufferedInputStream bis

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.is = socket.getInputStream();
            this.os = socket.getOutputStream();

            clientHandlers.add(this);

        }catch (IOException e){

        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try{
                InputStream inputStream = this.is;
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                BufferedImage bi = ImageIO.read(bis);


                broadcastMessage(bi);
                closeEverything(socket, bis);
            }catch(IOException e){
                break;
            }

        }
    }

    //Sends a message to all connected users in the clienthandlersList
    public void broadcastMessage(BufferedImage bi){

        Graphics graphics = bi.createGraphics();
        graphics.drawImage(bi, 0,0,null);
        graphics.dispose();


        for(ClientHandler clientHandler : clientHandlers){
            BufferedOutputStream bos = null;
            try{
                if(clientHandler != this){
                    bos = new BufferedOutputStream(clientHandler.os);

                    ImageIO.write(bi, "jpg", bos);


                }


            } catch (IOException e) {
                closeEverything(clientHandler.socket,bos);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("sERVER: " + clientUsername + "has left the chat");
    }

    public void closeEverything(Socket socket, BufferedInputStream bis){
        removeClientHandler();
        try{
            if(bis != null){
                bis.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
