import java.io.*;
import java.security.*;
import java.security.cert.Certificate;

class KeyHandler {

    public static void main(String[] args) {
        try {   
            String keystoreFile = args[0];
            String keyStorePassword  = args[1];
            String aliasName = args[2];
            String keyPassword = args[3];
            String dataFile = args[4];
            String certificateFile = args[5];
            String signatureFIle = args[6];


            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream keystoreIs = new FileInputStream(keystoreFile);
            FileInputStream dataFis = new FileInputStream(dataFile);
            byte[] data = dataFis.readAllBytes();
            dataFis.close();

            //The programmatic way to generate certificates is now deprecated. Therefore, the better way to do it is by using the command line tool "keytool" to request a certificate from a certificate authority
            keystore.load(keystoreIs, keyStorePassword.toCharArray());
            //alledgedly the private
            PrivateKey pvkey = (PrivateKey) keystore.getKey(aliasName, keyPassword.toCharArray());
            
            Certificate certificate =  keystore.getCertificate(aliasName);

            Signature s = Signature.getInstance("DSA");
            s.initSign(pvkey);
            s.update(data);
            byte[] signature = s.sign();

            ObjectOutputStream soos = new ObjectOutputStream(new FileOutputStream(signatureFIle));
            soos.writeObject(signature);
            soos.close();   
            
            ObjectOutputStream coos = new ObjectOutputStream(new FileOutputStream(certificateFile));
            coos.writeObject(certificate);
            coos.close();   

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}