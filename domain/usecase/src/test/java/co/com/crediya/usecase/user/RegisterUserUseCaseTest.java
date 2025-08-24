package co.com.crediya.usecase.user;

import co.com.crediya.model.common.ex.BusinessException;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

class RegisterUserUseCaseTest {

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

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
                .baseSalary(2000.0)
                .build();
    }

    @Test
    void mustFailWhenFirstNameIsNull() {
        User user = validUser.toBuilder().firstName(null).build();

        StepVerifier.create(registerUserUseCase.register(user))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.FIRSTNAME_REQUIRED.name()))
                .verify();
    }

    @Test
    void mustFailWhenLastNameIsNull() {
        User user = validUser.toBuilder().lastName(null).build();

        StepVerifier.create(registerUserUseCase.register(user))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.LASTNAME_REQUIRED.name()))
                .verify();
    }

    @Test
    void mustFailWhenEmailIsNull() {
        User user = validUser.toBuilder().email(null).build();

        StepVerifier.create(registerUserUseCase.register(user))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.EMAIL_REQUIRED.name()))
                .verify();
    }

    @Test
    void mustFailWhenEmailIsInvalid() {
        User user = validUser.toBuilder().email("invalid-email").build();

        StepVerifier.create(registerUserUseCase.register(user))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.INVALID_FORMAT.name()))
                .verify();
    }

    @Test
    void mustFailWhenSalaryIsInvalid() {
        User user = validUser.toBuilder().baseSalary(-100.0).build();

        StepVerifier.create(registerUserUseCase.register(user))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.INVALID_SALARY.name()))
                .verify();
    }

    @Test
    void mustFailWhenBirthDateIsNull() {
        User user = validUser.toBuilder().birthDate(null).build();

        StepVerifier.create(registerUserUseCase.register(user))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.BIRTHDATE_REQUIRED.name()))
                .verify();
    }

    @Test
    void mustFailWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(registerUserUseCase.register(validUser))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.EMAIL_ALREADY_REGISTERED.name()))
                .verify();
    }

    @Test
    void mustRegisterSuccessfully() {
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.save(validUser)).thenReturn(Mono.just(validUser));

        StepVerifier.create(registerUserUseCase.register(validUser))
                .expectNextMatches(user -> user.equals(validUser))
                .verifyComplete();
    }
}
