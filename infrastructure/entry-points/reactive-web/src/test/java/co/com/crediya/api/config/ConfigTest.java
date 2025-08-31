package co.com.crediya.api.config;

import co.com.crediya.api.user.UserHandler;
import co.com.crediya.api.user.UserRouterRest;
import co.com.crediya.api.dto.CreateUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.user.RegisterUserUseCase;
import co.com.crediya.usecase.user.ValidationUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {
        UserRouterRest.class,
        UserHandler.class,
        ValidationHandler.class
})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private ValidationUserUseCase validationUserUseCase;

    @MockitoBean
    private UserDTOMapper userDTOMapper;

    private CreateUserDTO createUserDTO;
    private User user;

    @BeforeEach
    void setUp() {
        createUserDTO = new CreateUserDTO(
                "Juan",
                "Pérez",
                LocalDate.of(1990, 1, 1),
                "Calle 123",
                "3001234567",
                "juan.perez@test.com",
                "100200300",
                BigDecimal.valueOf(1500000),
                "ADMINISTRADOR"
        );

        user = User.builder()
                .firstName(createUserDTO.firstName())
                .lastName(createUserDTO.lastName())
                .birthDate(createUserDTO.birthDate())
                .address(createUserDTO.address())
                .phone(createUserDTO.phone())
                .email(createUserDTO.email())
                .baseSalary(createUserDTO.baseSalary())
                .build();

        when(userDTOMapper.toModel(createUserDTO)).thenReturn(user);
        when(registerUserUseCase.register(user,"ADMINISTRADOR")).thenReturn(Mono.just(user));
    }

    @Test
    void securityHeadersShouldBeApplied() {
        webTestClient.get()
                .uri("/api/v1/users")
                .exchange()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }
}
