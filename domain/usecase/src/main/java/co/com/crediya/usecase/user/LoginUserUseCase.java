package co.com.crediya.usecase.user;


import co.com.crediya.model.common.gateways.PasswordEnconderService;
import co.com.crediya.model.common.gateways.TokenService;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEnconderService passwordEnconderService;

    public Mono<String> authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")))
                .flatMap(user -> {
                    if (passwordEnconderService.matches(rawPassword, user.getPassword())) {
                        return Mono.just(tokenService.generateToken(user));
                    } else {
                        return Mono.error(new RuntimeException("Invalid credentials"));
                    }
                });
    }


}