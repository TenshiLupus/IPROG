import java.io.*;
import java.net.*;
import javax.net.*;
import javax.net.ssl.*;

public class SSLSimpleClient {

    //Create a secure socket layer socket that encrypts teh data being sent from each node
    public static void main(String[] args) throws Exception {
        
        //Creates a socket from the default socket factory using the arguments passed in from the console ie keystore and password
        SocketFactory sf = SSLSocketFactory.getDefault();
        Socket s = sf.createSocket(args[0], Integer.parseInt(args[1]));
        
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter pw = new PrintWriter(s.getOutputStream());

        //basic testing
        System.out.println("Who is Sylvia?");
        pw.println("Who is Sylvia?");
        pw.flush();
        System.out.println(br.readLine());
        s.close();
    }
}
