package co.com.crediya.usecase.user;

import co.com.crediya.model.common.exception.BusinessException;
import co.com.crediya.model.common.gateways.PasswordEnconderService;
import co.com.crediya.model.common.gateways.TokenService;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class LoginUserUseCaseTest {

    @InjectMocks
    private LoginUserUseCase loginUserUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEnconderService passwordEnconderService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .build();
    }

    @Test
    void mustAuthenticateSuccessfully() {
        String rawPassword = "plain123";
        String expectedToken = "jwt-token-123";

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Mono.just(mockUser));
        when(passwordEnconderService.matches(rawPassword, mockUser.getPassword())).thenReturn(true);
        when(tokenService.generateToken(mockUser)).thenReturn(expectedToken);

        StepVerifier.create(loginUserUseCase.authenticate(mockUser.getEmail(), rawPassword))
                .expectNext(expectedToken)
                .verifyComplete();
    }

    @Test
    void mustFailWhenUserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Mono.empty());

        StepVerifier.create(loginUserUseCase.authenticate("notfound@example.com", "anyPass"))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.INVALID_CREDENTIALS.name()))
                .verify();
    }

    @Test
    void mustFailWhenPasswordDoesNotMatch() {
        String rawPassword = "wrongPassword";

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Mono.just(mockUser));
        when(passwordEnconderService.matches(rawPassword, mockUser.getPassword())).thenReturn(false);

        StepVerifier.create(loginUserUseCase.authenticate(mockUser.getEmail(), rawPassword))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.INVALID_CREDENTIALS.name()))
                .verify();
    }
}
