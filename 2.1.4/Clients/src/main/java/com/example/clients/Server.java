package com.example.clients;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Defines the server class that will accept incoming request s for connection and pass the sockets over to the clientHandler
public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){

        try {
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");

                ClientHandler clientHandler = new ClientHandler(socket);

                //Allocate the new client handler to a new thread
                Thread thread = new Thread(clientHandler);
                thread.start();
                System.out.println(ClientHandler.clientHandlers.size());

            }
        }//forced implementation of catch
        catch (IOException ioe){
            
        }
    }

    //close the resources when finished
    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //main thread of server that instantiates the server socket
    public static void main(String[] args) throws IOException{
       ServerSocket serverSocket = new ServerSocket(5000);
       Server server = new Server(serverSocket);
       server.startServer();

    }


}
