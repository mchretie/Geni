package ulb.infof307.g01.server.service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JWTService {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

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
