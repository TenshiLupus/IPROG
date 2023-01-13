package com.example.soundrecording;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

//Similar process to that of 2.1.4. Receives and sends out data in the form of a given length of byte data without closing any socket stream
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

                DataInputStream dis = new DataInputStream(this.is);

                //read the length of the file sent over from the client
                int fileLength = dis.readInt();
                if(fileLength > 0){
                    byte[] fileContent = new byte[fileLength];
                    dis.readFully(fileContent, 0, fileLength);
                    broadcastMessage(fileContent);
                }


            }catch(IOException e){
                e.printStackTrace();

                throw new RuntimeException("Error occurred in image reading");
            }
        }
    }

    //Sends a message to all connected users in the clienthandlersList
    public void broadcastMessage(byte[] data){

        for(ClientHandler clientHandler : clientHandlers){
            try{
                DataOutputStream dos = new DataOutputStream(clientHandler.os);
                dos.writeInt(data.length);
                dos.write(data);
                System.out.println("sending out image to clients");

            } catch (IOException e) {
                System.out.println("Error occurred in Broadcasting function");
                e.printStackTrace();

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