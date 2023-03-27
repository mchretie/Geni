package ulb.infof307.g01.model;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class User {
    private String username;
    private String password;
    private byte[] salt;

    public User(String username, String password) {
        try {
            this.username = username;
            this.salt = getSalt();
            this.password = getEncryptedPassword(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public User(String username, String password, String salt) {
        try {
            this.username = username;
            this.salt = salt.getBytes();
            Base64.Decoder decoder = Base64.getDecoder();
            this.salt = decoder.decode(salt);
            this.password = getEncryptedPassword(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }


    private String getEncryptedPassword(String unencryptedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 10000;
        int keyLength = 256;
        char[] password = unencryptedPassword.toCharArray();

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, this.salt, iterations, keyLength);
        SecretKey key = factory.generateSecret(spec);
        byte[] hashedPassword = key.getEncoded();

        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(hashedPassword);
    }

    private byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public String getSaltString() {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(this.salt);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
