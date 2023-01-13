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

        //Creates a new socket with with the given arguments to the program
        socket = new Socket(hostAddress, portNumber);

        //Sets the ability for the client output its input to teh other end of the thread
        clientOutput = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ClientThread ct = new ClientThread(socket);
        
        //Assigns the identifier of the  socket along with the input of the client
        //Sends it forward to the other end of the 
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
        //Closes all the utlized resoruces from the client
        socket.close();
        clientOutput.close();
        input.close();    
    }catch(IOException io){
        
    }

    }
}
