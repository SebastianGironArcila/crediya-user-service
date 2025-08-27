package co.com.crediya.usecase.user;

import co.com.crediya.model.common.exception.BusinessException;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidationUserUseCase {

    private final UserRepository userRepository;
    public Mono<User> getUserByIdentityDocument(String identityDocument) {
        return userRepository.findByIdentityDocument(identityDocument)
                .switchIfEmpty(Mono.error(BusinessException.Type.USER_NOT_FOUND.build()));
    }
}
