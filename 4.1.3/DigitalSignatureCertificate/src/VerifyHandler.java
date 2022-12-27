import java.io.*;
import java.security.*;
import java.security.cert.Certificate;

public class VerifyHandler {
    public static void main(String[] args) {
        String dataFile = args[0];
        String signatureFile = args[2];
        String certificateFile = args[1];
        
        try{
            FileInputStream dataFis = new FileInputStream(dataFile);
            byte[] origindata = dataFis.readAllBytes();
            dataFis.close();

            ObjectInputStream cois = new ObjectInputStream(new FileInputStream(certificateFile));
            ObjectInputStream sois = new ObjectInputStream(new FileInputStream(signatureFile));
            
            byte[] signature = (byte[]) sois.readObject(); 
            sois.close();
            Certificate certificate = (Certificate) cois.readObject();
            cois.close();
         
            PublicKey pbKey = certificate.getPublicKey();
            Signature s = Signature.getInstance("DSA"); 
            s.initVerify(pbKey);
            s.update(origindata);
            if(s.verify(signature)){
                System.out.println("Message was properly signed");
            }else{
                System.out.println("message was not accepted");
            }

        }   
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
