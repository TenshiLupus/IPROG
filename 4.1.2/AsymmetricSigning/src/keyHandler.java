import java.security.*;
import java.io.*;

public class keyHandler {
    public static void main(String[] args) {
        try{
        
        String privateKeyFile = args[0];
        String publicKeyFile = args[1];

        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
        
        kpGen.initialize(1024);

        KeyPair kp = kpGen.generateKeyPair();

        PrivateKey privateKeyData = kp.getPrivate();

        PublicKey publicKeyData = kp.getPublic();
        
        

        ObjectOutputStream privateKoos = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
        privateKoos.writeObject(privateKeyData);
        privateKoos.close();
        ObjectOutputStream publicKoos = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
        publicKoos.writeObject(publicKeyData);
        publicKoos.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

}