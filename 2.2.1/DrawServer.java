import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class DrawServer implements Runnable {

    public final static int SERVICE_PORT = 2000;
    public final static int BUFFER_SIZE = 1024;
    

    private DatagramSocket socket; 
    private ArrayList<InetAddress> clientAddresses;
    private ArrayList<Integer> clientPorts;
    private HashSet<String> existingClients;
    private Thread currentThread = new Thread(this);

    public DrawServer() throws IOException{
        socket = new DatagramSocket(SERVICE_PORT);
        clientAddresses = new ArrayList();
        clientPorts = new ArrayList();
        existingClients = new HashSet();
        currentThread.start();
    }

    public void run(){
        byte[] buffer = new byte[BUFFER_SIZE];
        while(true){
            try{
                Arrays.fill(buffer, (byte)0);
                DatagramPacket inputPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(inputPacket);
                
                String content = new String(inputPacket.getData());
                
                InetAddress clientAddress = inputPacket.getAddress();
                int clientPort = inputPacket.getPort();

                String id = clientAddress.toString() + "," + clientPort;
                if(!existingClients.contains(id)){
                    existingClients.add(id);
                    clientPorts.add(clientPort);
                    clientAddresses.add(clientAddress);

                }   

                System.out.println(id + " : " + content);
                byte[] data = content.getBytes();
                for(int i = 0; i < clientAddresses.size(); i++){
                    InetAddress cl = clientAddresses.get(i);
                    int cp = clientPorts.get(i);
                    inputPacket = new DatagramPacket(data, data.length, cl, cp);
                    socket.send(inputPacket);
                }

                System.out.println("Processed data");
            }catch(Exception e){

                System.out.println(e.getMessage());
            }
        }

    }
    public static void main(String[] args) throws Exception{
        DrawServer ds = new DrawServer();
        System.out.println("Ended main");
    }
}