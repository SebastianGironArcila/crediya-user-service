package co.com.crediya.usecase.user;

import co.com.crediya.model.common.ex.BusinessException;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Mono<User> register(User user) {
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            return Mono.error(BusinessException.Type.FIRSTNAME_REQUIRED.build());
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            return Mono.error(BusinessException.Type.LASTNAME_REQUIRED.build());
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return Mono.error(BusinessException.Type.EMAIL_REQUIRED.build());
        }
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            return Mono.error(BusinessException.Type.INVALID_FORMAT.build());
        }
        if (user.getBaseSalary() == null || user.getBaseSalary() < 0 || user.getBaseSalary() > 15_000_000) {
            return Mono.error(BusinessException.Type.INVALID_SALARY.build());
        }

        if (user.getBirthDate() == null) {
            return Mono.error(BusinessException.Type.BIRTHDATE_REQUIRED.build());
        }

        return userRepository.existsByEmail(user.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(BusinessException.Type.EMAIL_ALREADY_REGISTERED.build());                    }
                    return userRepository.save(user);
                });
    }
}
