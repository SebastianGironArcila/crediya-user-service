package co.com.crediya.model.common.gateways;

public interface PasswordEnconderService {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
