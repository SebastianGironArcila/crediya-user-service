package co.com.crediya.api.security;

import co.com.crediya.model.common.gateways.TokenService;
import co.com.crediya.model.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider implements TokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        if (jwtSecret == null || jwtSecret.length() < 32) {
            log.error("JWT secret is not configured properly");
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
        }
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Override
    public String generateToken(User user) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpiration);

            String roleName = mapRoleIdToRoleName(user.getRoleId());

            return Jwts.builder()
                    .subject(user.getEmail())
                    .claim("userId", user.getId())
                    .claim("roleId", user.getRoleId())
                    .claim("roleName", roleName)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getSigningKey(), Jwts.SIG.HS256)
                    .compact();

        } catch (Exception e) {
            log.error("Error generating JWT token for user: {}", user.getEmail(), e);
            throw new JwtException("Failed to generate token");
        }
    }

    private String mapRoleIdToRoleName(Integer roleId) {
        return switch (roleId) {
            case 1 -> "ADMINISTRATOR";
            case 2 -> "CUSTOMER";
            case 3 -> "ANALYST";
            case 4 -> "MANAGER";
            case 5 -> "SUPPORT";
            default -> "CUSTOMER";
        };
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT token");
        } catch (MalformedJwtException ex) {
            log.warn("Invalid JWT token");
        } catch (SecurityException ex) {
            log.warn("Invalid JWT signature");
        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims string is empty");
        }
        return false;
    }

    @Override
    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    @Override
    public Long getUserIdFromToken(String token) {
        Object userId = getClaimsFromToken(token).get("userId");
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }

    @Override
    public Long getRoleIdFromToken(String token) {
        Object roleId = getClaimsFromToken(token).get("roleId");
        return roleId != null ? Long.valueOf(roleId.toString()) : null;
    }

    @Override
    public String getRoleNameFromToken(String token) {
        Object roleName = getClaimsFromToken(token).get("roleName");
        return roleName != null ? roleName.toString() : null;
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("Error parsing JWT claims", e);
            throw new JwtException("Invalid token");
        }
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }
}