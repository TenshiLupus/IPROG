import java.net.Socket;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ServerThread implements Runnable {
    private Thread currentThread = new Thread(this);
    private Socket clientSocket;
    private ArrayList<ServerThread> threadList;
    public PrintWriter output;
    private BufferedReader input;

    private boolean clientThreadRunning;

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.clientSocket = socket;
        this.threadList = threads;
        this.clientThreadRunning = true;
        try {
            input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            output = new PrintWriter(this.clientSocket.getOutputStream(), true);
            
        } catch (IOException e) {
   
            e.printStackTrace();
        }
        currentThread.start();
        
    }

    //Runs on init
    @Override
    public void run() {
        try {
            while (clientThreadRunning) {
                //Read the input from the client into a variable
                String outputString = input.readLine();
                if(outputString != null){
                    printToAllClients(outputString);
                }
                if(outputString == null){
                    
                    threadList.remove(this);
                    break;
                }
            }
            printToAllClients("# A client has disconnected! #");
            Server.printServerCondition(Server.serverSocket, threadList);
          
        } catch (IOException e) {
            threadList.remove(this);
            String message = "A client has been forcefully isconnected by the server";
            printToAllClients(message);
            Server.printServerCondition(Server.serverSocket, threadList);
            stopThread();
        }
        finally{
            try {
                input.close();
                output.close();
                clientSocket.close();
            } catch (IOException e) {
                
                
            }
        }
    }

    // print out the received messages back to each client in the network
    // don't know why its not working
    private synchronized void printToAllClients(String outputString) {
        System.out.println(outputString);
        for (ServerThread st : threadList) {
            st.output.println(outputString);
        }
    }

    //Stop the running thread on request
    public synchronized void stopThread(){
        clientThreadRunning = false;
    }
}