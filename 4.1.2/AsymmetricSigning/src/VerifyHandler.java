import java.security.*;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.KeySpec;
import java.io.*;
import java.math.BigInteger;


public class VerifyHandler {
    public static void main(String[] args) {

        String dataFileName = args[0];
        String publicKeyFile = args[1];
        String signedDataFile = args[2];

        try {

            //Read all the data from the
            FileInputStream dataFis = new FileInputStream(dataFileName);
            byte[] data = dataFis.readAllBytes();
            dataFis.close();

            //Read the parameters that pertain to teh public key that will be utilized for signature verification
            ObjectInputStream publicKeydetes = new ObjectInputStream(new FileInputStream(publicKeyFile));
            BigInteger y = (BigInteger) publicKeydetes.readObject();
            BigInteger p = (BigInteger) publicKeydetes.readObject();
            BigInteger q = (BigInteger) publicKeydetes.readObject();
            BigInteger g = (BigInteger) publicKeydetes.readObject();
            publicKeydetes.close();

            //Get an instance of the keyfactory compatible with the public key
            KeyFactory kf = KeyFactory.getInstance("DSA");
            KeySpec publicKeySpec = new DSAPublicKeySpec(y, p, q, g);
            PublicKey pk = kf.generatePublic(publicKeySpec);

            //Retrieve the signature and data that will be verified
            ObjectInputStream signedData = new ObjectInputStream(new FileInputStream(signedDataFile));
            byte[] signature = (byte[]) signedData.readObject();
            byte[] signatureData = (byte[]) signedData.readObject();
            signedData.close();

            //Setup digest and verfication
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            Signature s = Signature.getInstance("DSA");
            s.initVerify(pk);
            md.update(data);
            byte[] oridigestBytes = md.digest();
            s.update(oridigestBytes);

            //Verifies that the message is valid and unchanged
            if(s.verify(signature)){
                System.out.println("Message was properly signed");
                if(MessageDigest.isEqual(oridigestBytes, signatureData)){
                    System.out.println("Contain same digest");
                }else{
                    System.out.println("are not same digest");
                }
            }else{
                System.out.println("File was not accepted");
            }  
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
