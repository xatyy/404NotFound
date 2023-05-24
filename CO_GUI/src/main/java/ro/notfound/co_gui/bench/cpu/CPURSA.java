package ro.notfound.co_gui.bench.cpu;

import ro.notfound.co_gui.bench.IBenchmark;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class CPURSA implements IBenchmark {
    private static final String ALGORITHM = "RSA";
    KeyPair keyPair;
    PublicKey publicKey;
    PrivateKey privateKey;
    String plaintext="";
    int keySize;

    public void initialize(Object... params) {
        try {
            keySize=(int)params[0];
            keyPair = generateKeyPair(keySize);
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        plaintext = "Hello, world!";
        try {
            String encryptedText = encrypt(plaintext, publicKey);
            String decryptedText = decrypt(encryptedText, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(Object... params) {
        plaintext = (String) params[0];
        try {
            String encryptedText = encrypt(plaintext, publicKey);
            String decryptedText = decrypt(encryptedText, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void warmUp() {
        plaintext = "Warm-up";
        try {
            String encryptedText = encrypt(plaintext, publicKey);
            String decryptedText = decrypt(encryptedText, privateKey);
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
        return "RSA benchmark complete.";
    }

    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = new SecureRandom();
        keyPairGenerator.initialize(keySize, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    private String encrypt(String plaintext, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        System.out.println("Encrypted text: " + Base64.getEncoder().encodeToString(encryptedBytes));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private String decrypt(String ciphertext, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        System.out.println("Decrypted text: " + new String(decryptedBytes));
        return new String(decryptedBytes);
    }
    public double score(double time,int nrKeys){
        double score=plaintext.length()*nrKeys*keySize/((time+1)*1000.0);
        return score;
    }
}
