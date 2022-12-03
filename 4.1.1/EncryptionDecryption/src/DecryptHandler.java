

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class DecryptHandler {
    
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException {
        
        String algorithm = "AES";
        String cipherEngine = "AES/ECB/NoPadding";
       
        // FileInputStream encryptedFileData = new FileInputStream(args[0]);
        // byte[] encryptedData = encryptedFileData.readAllBytes();
        // encryptedFileData.close();

        // System.out.println(encryptedData.length);



        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(args[1]));
        String b64encodedKey = (String) ois.readObject(); 
        ois.close();

        byte[] b64decodedKey = Base64.getDecoder().decode(b64encodedKey);
        Key originalKey = new SecretKeySpec(b64decodedKey, 0, b64decodedKey.length, algorithm);

        Cipher cipher = Cipher.getInstance(cipherEngine);
        cipher.init(Cipher.DECRYPT_MODE, originalKey);
        CipherInputStream cis = new CipherInputStream(new FileInputStream(args[0]), cipher);
        BufferedReader br = new BufferedReader(new InputStreamReader(cis, StandardCharsets.UTF_8));

        String message = br.readLine();
        
        System.out.println("DECRYPTION" + message);
        // byte[] decryptedOutput = cipher.doFinal(encryptedData);
        

        // System.out.println(decryptedOutput);

        System.out.println("Input: " + args[0]);

        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(args[2]+".txt"));
        PrintWriter pw = new PrintWriter(osw);
        pw.println(message);


    }

}
