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
            currentThread.start();
           
        }

        @Override
        public void run(){
            try{
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               
                while(true){
                    //use the 
                    String response = input.readLine();
                    System.out.println(response);
                    if(response.equals("exit")){
                        break;
                    };
                }
                
            }catch(IOException e){
                 // was not able to establish a connection
            }finally{
                try{
                    input.close();
                } catch (Exception e){
                    
                }
            }
        }
    }