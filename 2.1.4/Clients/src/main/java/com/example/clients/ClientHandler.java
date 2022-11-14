package com.example.clients;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
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

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.is = socket.getInputStream();
            this.os = socket.getOutputStream();

            System.out.println("outputstream " + os);
            clientHandlers.add(this);

        }catch (IOException e){

        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try{

                BufferedInputStream bis = new BufferedInputStream(this.is);

                if(bis != null) {
                    BufferedImage bi = ImageIO.read(bis);
                    bis.close();

                    if(bi != null) {
                        broadcastMessage(bi);
                    }
                }


            }catch(IOException e){
                e.printStackTrace();
                closeEverything(socket, is, os);
                throw new RuntimeException("Error occurred in image reading");
            }
        }
    }

    //Sends a message to all connected users in the clienthandlersList
    public void broadcastMessage(BufferedImage bi){

        for(ClientHandler clientHandler : clientHandlers){
            try{

                BufferedOutputStream bos = new BufferedOutputStream(clientHandler.os);
                ImageIO.write(bi, "jpg", bos);
                bos.close();

                System.out.println("sending out image to clients");

            } catch (IOException e) {
                System.out.println("Error occurred in Broadcasting function");
                e.printStackTrace();
                closeEverything(clientHandler.socket, clientHandler.is, clientHandler.os);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);

    }

    public void closeEverything(Socket socket, InputStream is, OutputStream os){
        removeClientHandler();
        try{
            if(is != null){
                is.close();
            }
            if(os != null){
                os.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
