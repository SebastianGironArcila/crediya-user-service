package co.com.crediya.usecase.user;

import co.com.crediya.model.common.exception.BusinessException;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

class ValidationUserUseCaseTest {

    @InjectMocks
    private ValidationUserUseCase validationUserUseCase;

    @Mock
    private UserRepository userRepository;

    private User validUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validUser = User.builder()
                .firstName("Dahiana")
                .lastName("Reyes")
                .birthDate(LocalDate.of(1990, 8, 23))
                .address("Calle 123")
                .phone("3001234567")
                .email("dahiana@example.com")
                .identityDocument("123456789")
                .baseSalary(BigDecimal.valueOf(2000.0))
                .roleId(1)
                .build();
    }

    @Test
    void mustReturnUserWhenFoundByIdentityDocument() {
        when(userRepository.findByIdentityDocument(validUser.getIdentityDocument()))
                .thenReturn(Mono.just(validUser));

        StepVerifier.create(validationUserUseCase.getUserByIdentityDocument(validUser.getIdentityDocument()))
                .expectNext(validUser)
                .verifyComplete();
    }

    @Test
    void mustFailWhenUserNotFoundByIdentityDocument() {
        String identityDocument = "999999999";

        when(userRepository.findByIdentityDocument(identityDocument))
                .thenReturn(Mono.empty());

        StepVerifier.create(validationUserUseCase.getUserByIdentityDocument(identityDocument))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.USER_NOT_FOUND.name()))
                .verify();
    }
}
