import java.net.Socket;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ServerThread implements Runnable {

    //Global variables
    private Thread currentThread = new Thread(this);
    private Socket clientSocket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    private boolean running;

    //assigns the helper varaibles to this individual thread for lcoal access to the passed in references rather than usign static methods for access
    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.clientSocket = socket;
        this.threadList = threads;
        this.running = true;
        currentThread.start();
        
    }

    //executes the below code on initiation of the thread
    @Override
    public void run() {
        try {
            // get the payload sent from the client
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //will update clients with all received messages
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            while (running) {
                String clientOutput = input.readLine();

                //notify all clients of new input
                printToAllClients(clientOutput);
            }

        } catch (Exception e) {
            System.out.println("Error occurred in the server class");
        }
    }

    //SERVER VIEW
    // print out the received messages back to each client in the network
    private void printToAllClients(String outputString) {
        for (ServerThread st : threadList) {
            st.output.println(this.clientSocket.getInetAddress().getHostName());
            st.output.println(outputString);
        }
    }

    //Helper method to kill the thread
    public void kill(){
        running = false;
    }
}