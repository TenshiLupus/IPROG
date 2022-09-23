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

    public static void main(String[] args) {
        final Socket serverPortConnection;
        final BufferedReader inbf;
        final PrintWriter clientOutput;
        final Scanner sc = new Scanner(System.in);

        // assign an end client ip address and provide a port on which to connect to
        // within the network

        // Since for this use case we are opening different terminals in the same
        // device. The ips should be the same. But in a real scenario. Different devices
        // would be assigned different ips within the same network range
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
        try {
            serverPortConnection = new Socket(hostAddress, portNumber);
            clientOutput = new PrintWriter(serverPortConnection.getOutputStream(), true);
            inbf = new BufferedReader(new InputStreamReader(serverPortConnection.getInputStream()));

            String userInput;
            
            while (active) {
                String message = ("(" + serverPortConnection.getInetAddress().getHostName() + ")" + "\n" + "says:");
                userInput = sc.nextLine();
                clientOutput.println(message + " " + userInput);
            }       
            serverPortConnection.close();
            inbf.close();
            clientOutput.close();
        } catch (IOException ioe) {
            System.out.println("Exception in Client class");
        }

    }
}
