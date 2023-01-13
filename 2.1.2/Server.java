import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.net.SocketException;

public class Server {

    //Global variables
    private static final Scanner sc = new Scanner(System.in);
    private static boolean running = true;
    public static ServerSocket serverSocket;

    //Instantiates the servers that holds references to all teh connected clients
    public static void main(String[] args) {
        System.out.println("Server is now running");
        ArrayList<ServerThread> threadList = new ArrayList<>();
        
        int socketPort = (args.length == 1) ? Integer.valueOf(args[0]) : 2000;
        
        //while the server is running establish conncetions to clients requesting a connection to the server port
        try {
            serverSocket = new ServerSocket(socketPort);

            //Server listening for port connections
            while (running) {
                
                //Once a cleint has beeen connected, update all clients the changes to teh server
                Socket clientPortConnection = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientPortConnection, threadList);
                threadList.add(serverThread);
                if (serverThread != null){
                   printServerCondition(serverSocket, threadList);
                }
            }
            serverSocket.close();
        } catch (SocketException se){
            //Ig fosmethings goes worng. Terminate the running time of the server
            shutdownServer();
        }catch (IOException ioe) {
          
            shutdownServer();
        } 
    }

    //shuts down the serevr
    private synchronized static void shutdownServer(){
        running = false;
    }

    //Presents how many clients are conencted to the server and prints out a message to all these clients
    public synchronized static void printServerCondition(ServerSocket serverSocket, ArrayList<ServerThread> threadList){
        
        String message = "Host: " + serverSocket.getInetAddress().getHostName() + " " + "Listening on port: " + serverSocket.getLocalPort() + " " + "Users connected: " + threadList.size();
        System.out.println(message);
        for(ServerThread st : threadList){
            st.output.println(message);
        }
    }

}
