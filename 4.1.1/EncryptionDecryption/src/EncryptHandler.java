

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EncryptHandler {
    
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException {
        
        //Encryption algorithm that will be used
        String algorithm = "AES";
        String cipherEngine = "AES/ECB/PKCS5Padding";
       
        //Read the content from the item to encrypt
        Path path = Paths.get(args[0]);
        String s = Files.readString(path);
        System.out.println("ENCRYPTION: " + s);

        //Read the key to encode the data with
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(args[1]));
        String b64encodedKey = (String) ois.readObject();
        ois.close();

        //Encode decode the key from base 64
        byte[] b64decodedKey = Base64.getDecoder().decode(b64encodedKey);

        //Retrieve the key from a keySpec wit hthe given data
        Key originalKey = new SecretKeySpec(b64decodedKey, 0, b64decodedKey.length, algorithm);

        //Create a cipher to encrypt the data with
        Cipher cipher = Cipher.getInstance(cipherEngine);
        cipher.init(Cipher.ENCRYPT_MODE, originalKey);
        
        //encrypt the data while streaming out the data to the desitnation file
        CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(args[2]), cipher);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(cos));
    
        pw.println("######");
        pw.println(s);
        pw.println("######");

        //close resource
        pw.close();
        

    }

}
