package com.example.clients;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

//Intermediary class that handles all the logic realted to the clients
public class ClientHandler implements Runnable{

    
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    
    //Global variables
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    //Assigns the correspondent resources to the client handler gets added to the shared ClientHandler list
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

    //
    @Override
    public void run() {

        //When reading images, it is adecuate to read teh given amount of array data at a time rather than closing the stream completely and losing the connection, such as in Buffered readers
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

    //Sends the image data to all connected users in the clienthandlersList
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

    //helper method to remove this client handler from the list
    public void removeClientHandler(){
        clientHandlers.remove(this);

    }

    //Clsoe all utilized resources
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
