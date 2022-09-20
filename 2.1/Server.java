import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

public class Server {
    private static final Scanner sc = new Scanner(System.in);
   
    public static void main(String[] args) throws IOException {
        final BufferedReader inbf;
        final PrintWriter out;

        ServerSocket serverSocket = new ServerSocket(2000);
        Socket acceptedPortConnection = serverSocket.accept();
        
        out = new PrintWriter(acceptedPortConnection.getOutputStream());
        inbf = new BufferedReader(new InputStreamReader(acceptedPortConnection.getInputStream()));


        //Create a new thread of execution
        Thread sender = new Thread(new Runnable(){
            
            String payload; //variable in which the response payload will be loaded

            //
            @Override
            public void run(){
                while(true){
                    payload = sc.nextLine(); //query input
                    out.println(payload); //pass the payload over to the accepted client socket
                    out.flush(); //clear the buffer from residual payload
                }


            }
        });
        sender.start();

        Thread receiver = new Thread( new Runnable(){

            String msg; //client payload

            @Override
            public void run(){
                try {
                msg = inbf.readLine(); // read the first line of the cleint payload

                //while there is still content to read in the payload read the next line
                while(msg!=null){
                    System.out.println("Client: " + msg);
                    msg = inbf.readLine();
                }
                System.out.println("Client disconnected");
                out.close();
                acceptedPortConnection.close();
                serverSocket.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

        });
        receiver.start();
        

        System.out.println("Client connected");

    }

}

