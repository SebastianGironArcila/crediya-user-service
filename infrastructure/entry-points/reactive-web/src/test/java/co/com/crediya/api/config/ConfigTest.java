package co.com.crediya.api.config;

import co.com.crediya.api.user.UserHandler;
import co.com.crediya.api.user.UserRouterRest;
import co.com.crediya.api.dto.CreateUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.user.RegisterUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private UserDTOMapper userDTOMapper;

    private CreateUserDTO createUserDTO;
    private User user;

    @BeforeEach
    void setUp() {
        createUserDTO = new CreateUserDTO(
                "Dahiana",
                "Reyes",
                LocalDate.of(1990, 8, 23),
                "Calle 123",
                "3001234567",
                "dahiana@example.com",
                2000.0
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

        // Simulamos el mapeo
        when(userDTOMapper.toModel(createUserDTO)).thenReturn(user);
        // Simulamos el registro
        when(registerUserUseCase.register(user)).thenReturn(Mono.just(user));
    }

    @Test
    void registerUserShouldReturnUser() {
        webTestClient.post()
                .uri("/api/v1/users") // ajusta al path real en tu router
                .bodyValue(createUserDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(response -> {
                    assert response != null;
                    assert response.getFirstName().equals(createUserDTO.firstName());
                    assert response.getEmail().equals(createUserDTO.email());
                });
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
