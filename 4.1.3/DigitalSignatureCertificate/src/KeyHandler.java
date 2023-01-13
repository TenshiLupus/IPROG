import java.io.*;
import java.security.*;
import java.security.cert.Certificate;

class KeyHandler {

    public static void main(String[] args) {
        try {   
            //File paths and argument values
            String keystoreFile = args[0];
            String keyStorePassword  = args[1];
            String aliasName = args[2];
            String keyPassword = args[3];
            String dataFile = args[4];
            String certificateFile = args[5];
            String signatureFIle = args[6];


        //Setup keystore and file data
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream keystoreIs = new FileInputStream(keystoreFile);
            FileInputStream dataFis = new FileInputStream(dataFile);
            byte[] data = dataFis.readAllBytes();
            dataFis.close();

            //The programmatic way to generate certificates is now deprecated. Therefore, the better way to do it is by using the command line tool "keytool" to request a certificate from a certificate authority
            //But we are doing it the programmatic way
            keystore.load(keystoreIs, keyStorePassword.toCharArray());
            //retreive the provate key from the keystore
            PrivateKey pvkey = (PrivateKey) keystore.getKey(aliasName, keyPassword.toCharArray());
            
            //Retreive a certificate with the given alias
            Certificate certificate =  keystore.getCertificate(aliasName);

            //Setup data for signing
            Signature s = Signature.getInstance("DSA");
            s.initSign(pvkey);
            s.update(data);
            byte[] signature = s.sign();

            //Outputs the signature file to a file
            ObjectOutputStream soos = new ObjectOutputStream(new FileOutputStream(signatureFIle));
            soos.writeObject(signature);
            soos.close();   
            
            //Outputs the certificate to its own file
            ObjectOutputStream coos = new ObjectOutputStream(new FileOutputStream(certificateFile));
            coos.writeObject(certificate);
            coos.close();   

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}