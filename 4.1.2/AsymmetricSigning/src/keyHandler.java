import java.security.*;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.io.*;
import java.math.BigInteger;

public class KeyHandler {
    public static void main(String[] args) {
        try{
        
        //KeyFile destinations
        String privateKeyFile = args[0];
        String publicKeyFile = args[1];

        //Generate a pair of keys
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("DSA");
        
        //Get the keys with the given size, multiple of 2
        kpGen.initialize(1024);
        KeyPair kp = kpGen.generateKeyPair();

        //Convert keys to DSA keys
        DSAPrivateKey privateKey = (DSAPrivateKey) kp.getPrivate();
        DSAPublicKey publicKey = (DSAPublicKey) kp.getPublic();
        
        //Setup params that will be utilized for later retrieval
        DSAParams privatedsaParams = privateKey.getParams();
        BigInteger x = privateKey.getX();
        BigInteger y = publicKey.getY();
        BigInteger p = privatedsaParams.getP();
        BigInteger q = privatedsaParams.getQ();
        BigInteger g = privatedsaParams.getG();

        // KeyFactory kf = KeyFactory.getInstance("DSA");
        // KeySpec privateKeySpec = new DSAPrivateKeySpec(x, p, q, g);
        
        //Output with streams the parameters to their respective keyFiles
        ObjectOutputStream privateKoos = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
        privateKoos.writeObject(x);
        privateKoos.writeObject(p);
        privateKoos.writeObject(q);
        privateKoos.writeObject(g);

        privateKoos.close();

        ObjectOutputStream publicKoos = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
        
        publicKoos.writeObject(y);
        publicKoos.writeObject(p);
        publicKoos.writeObject(q);
        publicKoos.writeObject(g);
        
        publicKoos.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

}