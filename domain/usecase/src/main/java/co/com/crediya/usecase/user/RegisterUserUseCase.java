package co.com.crediya.usecase.user;

import co.com.crediya.model.common.exception.BusinessException;
import co.com.crediya.model.common.gateways.PasswordEnconderService;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEnconderService passwordEnconderService;



    public Mono<User> register(User user, String roleName) {
        return userRepository.existsByEmail(user.getEmail())
                .flatMap(exists -> !exists
                        ? roleRepository.findByName(roleName)
                        .switchIfEmpty(Mono.error(BusinessException.Type.ROLE_NOT_FOUND.build()))
                        .flatMap(role -> {
                            user.setRoleId(role.getId());
                            user.setPassword(passwordEnconderService.encode(user.getPassword()));
                            return userRepository.save(user);
                        })
                        : Mono.error(BusinessException.Type.EMAIL_ALREADY_REGISTERED.build()));
    }
}
