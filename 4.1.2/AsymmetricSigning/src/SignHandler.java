import java.security.*;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.io.*;

public class SignHandler {
    public static void main(String[] args) {

        String dataFileName = args[0];
        String privateKeyFileName = args[1];
        String signatureFileOutput = args[2];

        try {

            FileInputStream fis = new FileInputStream(dataFileName);
            byte[] data = fis.readAllBytes();
            fis.close();

            FileInputStream pkfis = new FileInputStream(privateKeyFileName);
            byte[] privateKeyBytes = pkfis.readAllBytes();
            pkfis.close();

            KeyFactory kf = KeyFactory.getInstance("DSA");
            PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes,"DSA"));

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);

            byte[] digestBytes = md.digest();

            Signature s = Signature.getInstance("DSA");

            s.initSign(privateKey);
            
            byte[] buf = digestBytes;
            s.update(buf);

            byte[] signedDigestData = s.sign();
            

            ObjectOutputStream doos = new ObjectOutputStream(new FileOutputStream(signatureFileOutput));
            doos.writeObject(data);
            doos.writeObject(signedDigestData);
            

            //Read the objects from the Verify Handler and decode them there ## TODO tomorrow
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
