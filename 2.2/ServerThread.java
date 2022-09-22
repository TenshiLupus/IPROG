import java.net.Socket;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ServerThread implements Runnable {
    private Thread currentThread = new Thread(this);
    private Socket clientSocket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.clientSocket = socket;
        this.threadList = threads;
        currentThread.start();
    }

    //Runs on init
    @Override
    public void run() {
        try {
            // get the output passed from the client and turn it into input readable to the server thread.  
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //medium to return input from server as output to clients
            output = new PrintWriter(clientSocket.getOutputStream(), true);


            //if client writes exit, end session for the client/server in the loopback address
            while (true) {
                String outputString = input.readLine();

                //notify all clients of new input
                printToAllClients(outputString);

                System.out.printf("%s " + outputString);
            }

        } catch (Exception e) {
            System.out.println("Error occurred in the server class");
        }
    }

    // print out the received messages back to each client in the network
    private void printToAllClients(String outputString) {
        for (ServerThread st : threadList) {

            st.output.println(outputString);
        }
    }
}