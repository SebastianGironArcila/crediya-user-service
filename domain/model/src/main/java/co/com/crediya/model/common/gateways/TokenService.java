package co.com.crediya.model.common.gateways;

import co.com.crediya.model.user.User;

public interface TokenService
{
    String generateToken(User user);
    boolean validateToken(String token);
    String getEmailFromToken(String token);
    Long getUserIdFromToken(String token);
    Long getRoleIdFromToken(String token);
}
