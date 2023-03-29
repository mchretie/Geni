package ulb.infof307.g01.server.service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JWTService {

    // Singleton
    private static JWTService singleton = null;

    private JWTService() {}

    public static JWTService getInstance() {
        if (singleton == null) {
            singleton = new JWTService();
        }
        return singleton;
    }


    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // we might need to change this to file key

    public String generateToken(String username) {
        return Jwts.builder()
                    .setSubject(username)
                    .signWith(key)
                    .compact();
    }

    private Jws<Claims> getJwsClaims(String token) {
        return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
    }

    public Boolean isTokenValid(String token) {
        try {
            getJwsClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Jws<Claims> jwsClaims = getJwsClaims(token);
        return jwsClaims.getBody().getSubject();
    }
}
