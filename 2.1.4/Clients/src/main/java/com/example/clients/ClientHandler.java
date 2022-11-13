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

    private BufferedInputStream bis;
    private BufferedOutputStream bos;



    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.is = socket.getInputStream();
            this.os = socket.getOutputStream();
            this.bos = new BufferedOutputStream(os);
            this.bis = new BufferedInputStream(is);

            clientHandlers.add(this);

        }catch (IOException e){

        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try{
                BufferedImage bi = ImageIO.read(bis);

                Graphics graphics = bi.createGraphics();
                graphics.drawImage(bi,0,0,null);
                graphics.dispose();


                broadcastMessage(bi);

            }catch(IOException e){
                closeEverything(socket, bis, bos);
                break;
            }

        }
    }

    //Sends a message to all connected users in the clienthandlersList
    public void broadcastMessage(BufferedImage bi){

        for(ClientHandler clientHandler : clientHandlers){

            try{
                if(clientHandler != this){;
                    ImageIO.write(bi, "jpg", bos);

                }


            } catch (IOException e) {
                closeEverything(socket, bis, bos);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);

    }

    public void closeEverything(Socket socket, BufferedInputStream bis, BufferedOutputStream bos){
        removeClientHandler();
        try{
            if(bis != null){
                bis.close();
            }
            if(bos != null){
                bos.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
