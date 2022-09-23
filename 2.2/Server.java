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
    private static final Scanner sc = new Scanner(System.in);
    private static boolean running = true;
    public static ServerSocket serverSocket;

    public static void main(String[] args) {
        System.out.println("Server is now running");
        ArrayList<ServerThread> threadList = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(2000);

            while (running) {
                
                Socket clientPortConnection = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientPortConnection, threadList);
                threadList.add(serverThread);
                if (serverThread != null){
                   printServerCondition(serverSocket, threadList);
                }
            }
            serverSocket.close();
        } catch (SocketException se){
            se.printStackTrace();
            shutdownServer();
        }catch (IOException ioe) {
            ioe.printStackTrace();
            shutdownServer();
        } 
    }

    private static void shutdownServer(){
        running = false;
    }

    public static void printServerCondition(ServerSocket serverSocket, ArrayList<ServerThread> threadList){
        System.out.println("Host: " + serverSocket.getInetAddress().getHostName() + " " + "Listening on port: " + serverSocket.getLocalPort() + " " + "Users connected: " + threadList.size());
    }

}
