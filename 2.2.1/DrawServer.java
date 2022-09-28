import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class DrawServer {

    public final static int SERVICE_PORT = 2000;

    public static void main(String[] args){
        try{
            DatagramSocket serverSocket = new DatagramSocket(SERVICE_PORT);
            byte[] receivingDataBuffer = new byte[1024];
            byte[] sendingDataBuffer = new byte[1024];

            DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            System.out.println("Listening on client connection");

            serverSocket.receive(inputPacket);
            
            //log client data sent by the client
            String receiveData = new String(inputPacket.getData());
            System.out.println("Sent from the client: " + receiveData);

            sendingDataBuffer = receiveData.getBytes();

            InetAddress senderAddress = inputPacket.getAddress();
            int senderPort = inputPacket.getPort();

            DatagramPacket outPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length, senderAddress, senderPort);

            serverSocket.send(outPacket);

            serverSocket.close();


        }catch(SocketException se){
            se.getMessage();
        }catch(IOException ie){
            ie.getMessage();
        }
    }
}