import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.InputStreamReader;

public class Client {

    public static void main(String[] args) throws IOException {
        final Socket serverPortConnection;
        final BufferedReader inbf;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);

        // assign an end client ip address and provide a port on which to connect to
        // within the network
        serverPortConnection = new Socket("127.0.0.1", 2000);
        out = new PrintWriter(serverPortConnection.getOutputStream());
        inbf = new BufferedReader(new InputStreamReader(serverPortConnection.getInputStream()));

        // Create a new thread of execution
        Thread sender = new Thread(new Runnable() {

            String payload; // variable in which the response payload will be loaded

            //
            @Override
            public void run() {
                while (true) {
                    payload = sc.nextLine(); // query input
                    out.println(payload); // pass the payload over to the server socket
                    out.flush(); // clear the buffer from residual payload
                }

            }
        });

        sender.start();

        Thread receiver = new Thread(new Runnable() {

            String msg; // client payload

            @Override
            public void run() {
                try {
                    msg = inbf.readLine(); // read the first line of the cleint payload

                    // while there is still content to read in the payload read the next line
                    while (msg != null) {
                        System.out.println("Server: " + msg);
                        msg = inbf.readLine();
                    }
                    System.out.println("Session with server has ended");
                    out.close();
                    serverPortConnection.close();
                 
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        receiver.start();
    }
}
