

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
        
        String algorithm = "AES";
        String cipherEngine = "AES/ECB/PKCS5Padding";
       
        Path path = Paths.get(args[0]);
        String s = Files.readString(path);
        System.out.println("ENCRYPTION: " + s);

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(args[1]));
        String b64encodedKey = (String) ois.readObject();
        ois.close();

        byte[] b64decodedKey = Base64.getDecoder().decode(b64encodedKey);

        Key originalKey = new SecretKeySpec(b64decodedKey, 0, b64decodedKey.length, algorithm);

        Cipher cipher = Cipher.getInstance(cipherEngine);
        cipher.init(Cipher.ENCRYPT_MODE, originalKey);
        CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(args[2]), cipher);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(cos));
    
        pw.println("######");
        pw.println(s);
        pw.println("######");

        pw.close();
        

        // byte[] encryptedData = cipher.doFinal(data);
        
        // System.out.println(encryptedData.length);

        // try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(args[2] +".txt"))) {
        //     oos.writeObject(encryptedData);
        //     oos.close();
        // } catch (IOException e){
        //     e.printStackTrace();
        // }

        // cipher.init(Cipher.DECRYPT_MODE, key);
        // byte[] decryptedOutput = cipher.doFinal(encryptedOutput);
        // System.out.println(decryptedOutput);

        // System.out.println("Input: " + args[0]);
     

    }

}
