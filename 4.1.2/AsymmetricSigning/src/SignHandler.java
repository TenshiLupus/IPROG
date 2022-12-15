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

        String dataFileName = args[0];
        String privateKeyFileName = args[1];
        String signatureFileOutput = args[2];

        try {

            FileInputStream dataFis = new FileInputStream(dataFileName);
            byte[] data = dataFis.readAllBytes();
            dataFis.close();

            ObjectInputStream privateKeydetes = new ObjectInputStream(new FileInputStream(privateKeyFileName));
            BigInteger x = (BigInteger) privateKeydetes.readObject();
            BigInteger p = (BigInteger) privateKeydetes.readObject();
            BigInteger q = (BigInteger) privateKeydetes.readObject();
            BigInteger g = (BigInteger) privateKeydetes.readObject();

            privateKeydetes.close();

            KeyFactory kf = KeyFactory.getInstance("DSA");
            KeySpec privateKeySpec = new DSAPrivateKeySpec(x, p, q, g);
            PrivateKey pk = kf.generatePrivate(privateKeySpec);
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);

            //Original data digest
            byte[] digestBytes = md.digest();

            Signature s = Signature.getInstance("DSA");

            s.initSign(pk);
            
            byte[] buf = digestBytes;
            s.update(buf);

            byte[] signature = s.sign();
            

            ObjectOutputStream doos = new ObjectOutputStream(new FileOutputStream(signatureFileOutput));
            doos.writeObject(signature);
            doos.writeObject(digestBytes);
            doos.close();
            

            //Read the objects from the Verify Handler and decode them there ## TODO tomorrow
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
