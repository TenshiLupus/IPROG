import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Server {
    private static final Scanner sc = new Scanner(System.in);
    private static boolean running = true;

    public static void main(String[] args) {
        System.out.println("Server is now running");
        ArrayList<ServerThread> threadList = new ArrayList<>();

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(2000);

            while (running) {
                Socket clientPortConnection = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientPortConnection, threadList);
                threadList.add(serverThread);
            }
            serverSocket.close();
        } catch (IOException e) {
           
            e.printStackTrace();
            shutdownServer();
        }
    }

    private static void shutdownServer(){
        running = false;
    }

}
