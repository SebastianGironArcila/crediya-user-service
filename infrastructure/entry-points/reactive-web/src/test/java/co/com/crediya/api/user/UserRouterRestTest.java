package co.com.crediya.api.user;

import co.com.crediya.api.dto.CreateUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.user.RegisterUserUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class})
@WebFluxTest
class UserRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private UserDTOMapper userDTOMapper;

    @Test
    void shouldRegisterUserSuccessfully() {
        // Creamos el DTO como record
        CreateUserDTO dto = new CreateUserDTO(
                "Juan",
                "Pérez",
                LocalDate.of(1990, 1, 1),
                "Calle 123",
                "3001234567",
                "juan.perez@test.com",
                3500.0
        );

        // Creamos el modelo User
        User userModel = User.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .birthDate(dto.birthDate())
                .address(dto.address())
                .phone(dto.phone())
                .email(dto.email())
                .baseSalary(dto.baseSalary())
                .build();

        // Simulamos mapper y caso de uso
        when(userDTOMapper.toModel(any(CreateUserDTO.class))).thenReturn(userModel);
        when(registerUserUseCase.register(any(User.class))).thenReturn(Mono.just(userModel));

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(userResponse -> {
                    Assertions.assertThat(userResponse.getEmail()).isEqualTo(dto.email());
                    Assertions.assertThat(userResponse.getFirstName()).isEqualTo(dto.firstName());
                });
    }
}
