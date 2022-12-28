import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.*;
import javax.net.ssl.*;

public class SSLSimpleServer extends Thread {
    
    private Socket sock;
    
    public static void main(String[] args) throws Exception {

        //Instatioate a SSL server socket to accept incoming conenctions from the clients
        ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
        ServerSocket ss = ssf.createServerSocket(9096);
        
        while (true) {
            new SSLSimpleServer(ss.accept()).start();
        }
    }


    public SSLSimpleServer(Socket s) {
        sock = s;
    }

    public void run() {
        try {
            
            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter pw = new PrintWriter(sock.getOutputStream());
            String data = br.readLine();
           
            pw.println("What is she?");
            pw.close();
            sock.close();
        } catch (IOException ioe) {
            // Client disconnected; exit this thread
        }
    }
}
