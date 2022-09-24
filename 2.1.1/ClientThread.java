import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ClientThread implements Runnable{

        private Thread currentThread = new Thread(this);
        private Socket socket;
        private BufferedReader input;

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