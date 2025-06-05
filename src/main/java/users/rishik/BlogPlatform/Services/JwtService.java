package users.rishik.BlogPlatform.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Security.JwtConfig;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@ConfigurationProperties(prefix = "jwt")
@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    JwtService(JwtConfig jwtConfig){
        this.jwtConfig = jwtConfig;
    }

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(this.jwtConfig.getSecret().getBytes());
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(this.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean isValidToken(String token, String email) {
        return email.equals(extractUsername(token)) && !this.isExpired(token);
    }
}
