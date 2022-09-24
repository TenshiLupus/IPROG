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
        final Socket socket;
        final BufferedReader input;
        final PrintWriter clientOutput;
        final Scanner sc = new Scanner(System.in);
        System.out.println(args.length);

        // assign an end client ip address and provide a port on which to connect to
        // within the network

        // Since for this use case we are opening different terminals in the same
        // device. The ips should be the same. But in a real scenario. Different devices
        // would be assigned different ips within the same network range
        String hostAddress = "127.0.0.1";
        int portNumber = 2000;

        switch (args.length) {
            case 1:
                hostAddress = args[0];
                System.out.println("host address changed");
                break;

            case 2:
                hostAddress = args[0];
                System.out.println("host addres and port changed");
                portNumber = Integer.valueOf(args[1]);
                break;

            default:
                break;
        }

        try{

        socket = new Socket(hostAddress, portNumber);
        clientOutput = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ClientThread ct = new ClientThread(socket);
        
        String userInput;

        while (active) {
            String message = ("(" + socket.getInetAddress().getHostName() + ")" + "\n" + "says: ");
            
            userInput = sc.nextLine();
            if (userInput.equals("exit")) {
                break;
            } else {
                clientOutput.println(message + userInput);
            }
        }
        socket.close();
        clientOutput.close();
        input.close();    
    }catch(IOException io){
        
    }

    }
}
