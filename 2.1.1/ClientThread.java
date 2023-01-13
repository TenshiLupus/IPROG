import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

//Defines a Client thread class with the interface implementation
public class ClientThread implements Runnable{

        //Glboal variables
        private Thread currentThread = new Thread(this);
        private Socket socket;
        private BufferedReader input;

        //Utilizea buffered reader for the output of the client
        public ClientThread(Socket socket) throws IOException{
            this.socket = socket;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            currentThread.start();
        }

        @Override
        public void run(){

            try{
                while(true){
                    //Store what was passed to the sockets buffered reader and output it
                    String response = input.readLine();
                    System.out.println(response);
                }
            }catch(IOException e){
                e.printStackTrace(); // was not able to establish a connection
            }finally{
                try{
                    input.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }