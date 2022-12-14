import java.security.*;
import java.io.*;


public class VerifyHandler {
    public static void main(String[] args) {

        String unsignedDataFile = args[0];
        String privateKeyFile = args[1];
        String signedDataFile = args[2];

        try {

            ObjectInputStream fis = new ObjectInputStream(new FileInputStream(unsignedDataFile));
            byte[] data = fis.readAllBytes();
            fis.close();

            ObjectInputStream pkois = new ObjectInputStream(new FileInputStream(privateKeyFile));
            PublicKey publicKey = (PublicKey) pkois.readObject();
            pkois.close();

            ObjectInputStream signedData = new ObjectInputStream(new FileInputStream(unsignedDataFile));
            byte[] signature = (byte[]) signedData.readObject();

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            Signature s = Signature.getInstance("DSA");
            s.initVerify(publicKey);
            
            md.update(data);
            byte[] oridigestBytes = md.digest();
            
            s.update(data);
            //Verifies that the messages is unchanged
            if(s.verify(signature)){
                System.out.println("Message was properly signed");
                if(MessageDigest.isEqual(oridigestBytes, signature)){
                    System.out.println("Contain same digest");
                }else{
                    System.out.println("are not same digest");
                }
            }else{
                System.out.println("File was not accepted");
            }  
            

            //Read the objects from the Verify Handler and decode them there ## TODO tomorrow
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
