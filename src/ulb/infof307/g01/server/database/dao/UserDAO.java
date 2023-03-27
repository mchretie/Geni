package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.server.database.Database;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

public class UserDAO {
    private final static Database database = Database.singleton();

    public void registerGuest(UUID guestId) throws SQLException {
        String sql = """
                INSERT INTO user (user_id, username, password)
                VALUES ('%1$s', 'guest', 'guest')
                """.formatted(guestId.toString());

        database.executeUpdate(sql);
    }

    public boolean userExists(UUID userId) throws SQLException {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM user
                    WHERE user_id = '%1$s'
                )
                """.formatted(userId.toString());

        try (ResultSet res = database.executeQuery(sql)) {
            return res.getBoolean(1);
        }
    }

    public boolean usernameExists(String username) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM user
                    WHERE username = '%1$s'
                )
                """.formatted(username);

        try (ResultSet res = database.executeQuery(sql)) {
            return res.getBoolean(1);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean registerUser(String username, String password) {
        if (usernameExists(username)) return false;

        User user = new User(username, password);
        UUID user_id = UUID.randomUUID();

        // language=SQL
        String sql = """
                INSERT INTO user (user_id, username, password, salt)
                VALUES ('%1$s', '%2$s', '%3$s', '%4$s')
                """.formatted(user_id.toString(), user.getUsername(), user.getPassword(), user.getSaltString());

        try {
            database.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

class User {
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

    public User(String username, String password, byte[] salt) {
        try {
            this.username = username;
            this.salt = salt;
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