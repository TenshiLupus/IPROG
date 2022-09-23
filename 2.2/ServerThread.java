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

    private boolean clientThreadRunning;

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.clientSocket = socket;
        this.threadList = threads;
        this.clientThreadRunning = true;
        currentThread.start();
        
    }

    //Runs on init
    @Override
    public void run() {
        try {
            // get the written messages from the client
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //server output all messages back to client
            output = new PrintWriter(clientSocket.getOutputStream(), true);


            
            while (clientThreadRunning) {
                //Read the input from the client into a variable
                String outputString = input.readLine();
                System.out.println(outputString);
                
                if(outputString == null || outputString.toLowerCase().trim().equalsIgnoreCase(outputString)){
                    threadList.remove(this);
                    stopThread();
                    break;
                }
                //present all clients with new input
                printToAllClients(outputString);

                //log message to server console
                
            }
            threadList.remove(this);
            System.out.println(threadList.size());
            printToAllClients("# A client has disconnected! #");
            Server.printServerCondition(Server.serverSocket, threadList);
            clientSocket.close();
            output.close();
            input.close();

        } catch (Exception e) {
            System.out.println("Error occurred in the serverThread class");
            stopThread();
        }
    }

    // print out the received messages back to each client in the network
    private void printToAllClients(String outputString) {
        for (ServerThread st : threadList) {
            st.output.println(outputString);
        }
    }

    //Stop the running thread on request
    public void stopThread(){
        clientThreadRunning = false;
    }
}