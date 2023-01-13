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

    //Initiates the server 
    public static void main(String[] args) throws IOException {
        
        //helper variables
        System.out.println("Server is now running");
        final BufferedReader inbf;
        final PrintWriter out;
        ArrayList<ServerThread> threadList = new ArrayList<>();

        //Initiates a new serversocket that listens on the given port
        ServerSocket serverSocket = new ServerSocket(2000);
        //Consattnly listen for new incoming connection requests to the serversocket port
        while (true) {
            
            //Creates a serverside socket that has a connection to the requesting client
            Socket acceptedPortConnection = serverSocket.accept();
            ServerThread serverThread = new ServerThread(acceptedPortConnection, threadList);

            //adds the client thread to the list of conencted clients
            threadList.add(serverThread);
        }

    }

}
