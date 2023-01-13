import java.security.*;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.io.*;
import java.math.BigInteger;

public class SignHandler {
    public static void main(String[] args) {
        
        //File paths
        String dataFileName = args[0];
        String privateKeyFileName = args[1];
        String signatureFileOutput = args[2];

        try {
            //Get the data from the file that wil be signed
            FileInputStream dataFis = new FileInputStream(dataFileName);
            byte[] data = dataFis.readAllBytes();
            dataFis.close();

            //Convert the key parameters to the appropiate formate for uasge in the keyspecs
            ObjectInputStream privateKeydetes = new ObjectInputStream(new FileInputStream(privateKeyFileName));
            BigInteger x = (BigInteger) privateKeydetes.readObject();
            BigInteger p = (BigInteger) privateKeydetes.readObject();
            BigInteger q = (BigInteger) privateKeydetes.readObject();
            BigInteger g = (BigInteger) privateKeydetes.readObject();

            privateKeydetes.close();

            //genereate a private key
            KeyFactory kf = KeyFactory.getInstance("DSA");
            KeySpec privateKeySpec = new DSAPrivateKeySpec(x, p, q, g);
            PrivateKey pk = kf.generatePrivate(privateKeySpec);
            
            //retrieve the data of the file that will be verified for changes
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);

            //Original data digest
            byte[] digestBytes = md.digest();

            Signature s = Signature.getInstance("DSA");

            //Setup the signature and sign the data
            s.initSign(pk);
            byte[] buf = digestBytes;
            s.update(buf);
            byte[] signature = s.sign();
            
            //Output the signed data to a file
            ObjectOutputStream doos = new ObjectOutputStream(new FileOutputStream(signatureFileOutput));
            doos.writeObject(signature);
            doos.writeObject(digestBytes);
            doos.close();
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
