

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
        
        //Settings that are used for the Cipher
        String algorithm = "AES";
        String cipherEngine = "AES/ECB/PKCS5Padding";

        //Retrieve the unencrypted key from the file
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(args[1]));
        String b64encodedKey = (String) ois.readObject(); 
        ois.close();

        //Formatting key data from the file into a compatible key format.
        byte[] b64decodedKey = Base64.getDecoder().decode(b64encodedKey);
        Key originalKey = new SecretKeySpec(b64decodedKey, 0, b64decodedKey.length, algorithm);

        //Initiate the cipher that will be used for decryption
        Cipher cipher = Cipher.getInstance(cipherEngine);
        cipher.init(Cipher.DECRYPT_MODE, originalKey);

        //Due to stream corruption with "cipher.doFinal()", a CipherInputStream is being used as an alternative
        CipherInputStream cis = new CipherInputStream(new FileInputStream(args[0]), cipher);
        BufferedReader br = new BufferedReader(new InputStreamReader(cis));
        
        //Prepare writing to a file with the same format for this use case
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(args[2]+".txt"));
        PrintWriter pw = new PrintWriter(osw);

        //Writes retrieved content into the output file.
        String msg;
        while ((msg = br.readLine()) != null){
            pw.println(msg);
        }

        osw.close();
        pw.close();

    }

}
