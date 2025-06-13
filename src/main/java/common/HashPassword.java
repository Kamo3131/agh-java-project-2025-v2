package common;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class HashPassword {
    private final char[] password;
    private final byte[] salt;
    private final int iterations;
    private final int keyLength;


    public byte[] getSalt(){
        return salt;
    }
    public HashPassword(String password, int saltLength, int iterations, int keyLength) {
        this.password = password.toCharArray();
        this.salt = generateSalt(saltLength);
        this.iterations = iterations;
        this.keyLength = keyLength;
    }
    public HashPassword(String password, byte[] salt, int iterations, int keyLength) {
        this.password = password.toCharArray();
        this.salt = salt;
        this.iterations = iterations;
        this.keyLength = keyLength;
    }
    private byte[] generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] generateHash(){
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public String hash() {
        byte[] hashedBytes = generateHash();
        return iterations + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hashedBytes)+":"+keyLength;
    }
    public boolean verify(String newPassword, String hashedPassword) {
        String[] splitHash = hashedPassword.split(":");
        int iterations = Integer.parseInt(splitHash[0]);
        byte[] salt = Base64.getDecoder().decode(splitHash[1]);
        byte[] hashedBytes = Base64.getDecoder().decode(splitHash[2]);
        int keyLength = Integer.parseInt(splitHash[3]);

        HashPassword hashPassword = new HashPassword(newPassword, salt, iterations, keyLength);
        String newHashedPassword = hashPassword.hash();
        String[] splitNewHash = newHashedPassword.split(":");
        byte[] newHashedBytes = Base64.getDecoder().decode(splitNewHash[2]);
        return MessageDigest.isEqual(newHashedBytes, hashedBytes);
    }
}
