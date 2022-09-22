import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.InputStreamReader;

public class Client {
    private static boolean active = true;
    // For a network with mask 255.255.255.0 /24 we can assume there are 255
    // available host adresses in the loopback range. Therefore a byte suffices
    // private static byte hostNumber = 1;

    public static void main(String[] args) throws IOException {
        final Socket serverPortConnection;
        final BufferedReader inbf;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);

        // assign an end client ip address and provide a port on which to connect to
        // within the network

        String hostAddress = "127.0.0.1";
        int portNumber = 2000;

        switch (args.length) {
            case 3:
                hostAddress = args[2];
                break;

            case 4:
                hostAddress = args[2];
                portNumber = Integer.valueOf(args[3]);
                break;

            default:
                break;
        }

        serverPortConnection = new Socket(hostAddress, portNumber);
        out = new PrintWriter(serverPortConnection.getOutputStream(), true);
        inbf = new BufferedReader(new InputStreamReader(serverPortConnection.getInputStream()));

        String userInput;
        String response;
        String clientName = null;

        while (active) {
            String message = ("" + " message: ");
            userInput = sc.nextLine();
            out.println(message + " " + userInput);
        }
        serverPortConnection.close();
         
    }
}
