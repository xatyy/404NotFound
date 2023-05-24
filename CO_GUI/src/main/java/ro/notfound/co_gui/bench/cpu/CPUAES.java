package ro.notfound.co_gui.bench.cpu;

import ro.notfound.co_gui.bench.IBenchmark;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class CPUAES implements IBenchmark {
    private static final String ALGORITHM = "AES";
    SecretKey secretKey;
    String plaintext="";
    int key;

    public void initialize(Object... params) {
        try {
            secretKey = generateKey((int)params[2]);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        plaintext = "Hello, world!";
        try {
            String encryptedText = encrypt(plaintext, secretKey);
            String decryptedText = decrypt(encryptedText, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(Object... params) {
        plaintext = (String) params[0];
        try {
            String encryptedText = encrypt(plaintext, secretKey);
            String decryptedText = decrypt(encryptedText, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void warmUp() {
        plaintext = "Warm-up";
        try {
            String encryptedText = encrypt(plaintext, secretKey);
            String decryptedText = decrypt(encryptedText, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clean() {
        // no clean-up necessary
    }

    public void cancel() {
        // no cancellation necessary
    }

    public String getResult() {
        return "AES benchmark complete.";
    }

    public static SecretKey generateKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = new SecureRandom();
        //int keySize = 128;
        keyGenerator.init(keySize, secureRandom);
        System.out.println("Key: " + keyGenerator.generateKey());
        return keyGenerator.generateKey();
    }

    private String encrypt(String plaintext, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        System.out.println("Encrypted text: " + Base64.getEncoder().encodeToString(encryptedBytes));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private String decrypt(String ciphertext, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        System.out.println("Decrypted text: " + new String(decryptedBytes));
        return new String(decryptedBytes);
    }
    public double score(double time,int keySize){
        double score=plaintext.length()*keySize/((time+1)*100.0);
        return score;
    }

}