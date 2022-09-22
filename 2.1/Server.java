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

    public static void main(String[] args) throws IOException {
        System.out.println("Server is now running");
        final BufferedReader inbf;
        final PrintWriter out;
        ArrayList<ServerThread> threadList = new ArrayList<>();

        ServerSocket serverSocket = new ServerSocket(2000);
        while (true) {
            
            Socket acceptedPortConnection = serverSocket.accept();
            if(acceptedPortConnection != null){
                
            }
            ServerThread serverThread = new ServerThread(acceptedPortConnection, threadList);
            threadList.add(serverThread);
        }

    }

}
