

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;

public class KeyHandler {
    
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
        String algorithm = "AES";
        String cipherEngine = "AES/ECB/PKCS5Padding";


        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        kg.init(192);
        Key key = kg.generateKey();
        
        String keyData = Base64.getEncoder().encodeToString(key.getEncoded());
        
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(args[0]+".txt"));
        oos.writeObject(keyData);

        oos.close();

        // Cipher cipher = Cipher.getInstance(cipherEngine);
        // cipher.init(Cipher.ENCRYPT_MODE, key);
        // byte[] encryptedOutput = cipher.doFinal(data);
        // System.out.println(encryptedOutput);

        // cipher.init(Cipher.DECRYPT_MODE, key);
        // byte[] decryptedOutput = cipher.doFinal(encryptedOutput);
        // System.out.println(decryptedOutput);

        // System.out.println("Input: " + args[0]);
     

    }

}
