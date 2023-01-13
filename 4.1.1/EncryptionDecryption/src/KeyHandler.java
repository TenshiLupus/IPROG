

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;

public class KeyHandler {
    
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
        //Encryption algorithm
        String algorithm = "AES";
        String cipherEngine = "AES/ECB/PKCS5Padding";

        //Generate a key of a given size
        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        kg.init(192);
        Key key = kg.generateKey();
        
        // encrypt the key as base64 and store it in the keyFile
        String keyData = Base64.getEncoder().encodeToString(key.getEncoded());
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(args[0]+".txt"));
        oos.writeObject(keyData);

        oos.close();
    }

}
