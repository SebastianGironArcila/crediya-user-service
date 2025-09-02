package co.com.crediya.api.user;

import co.com.crediya.api.config.GlobalExceptionHandler;
import co.com.crediya.api.config.ValidationHandler;
import co.com.crediya.api.dto.CreateUserDTO;
import co.com.crediya.api.dto.UserDTO;
import co.com.crediya.api.dto.UserIdentityDocumentDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.user.RegisterUserUseCase;
import co.com.crediya.usecase.user.ValidationUserUseCase;
import jakarta.validation.*;
import org.assertj.core.api.Assertions;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class,GlobalExceptionHandler.class})
@WebFluxTest
class UserRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;


    @MockitoBean
    private ValidationUserUseCase validationUserUseCase;

    @MockitoBean
    private UserDTOMapper userDTOMapper;

    @MockitoBean
    private ValidationHandler validationHandler;


    private final  CreateUserDTO createUserDTO = CreateUserDTO.builder()
            .firstName("Juan")
            .lastName("Pérez")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("Calle 123")
            .phone("3001234567")
            .email("juan.perez@test.com")
            .identityDocument("100200300")
            .baseSalary(BigDecimal.valueOf(1500000))
            .roleName("ADMINISTRADOR")
            .build();


    private final User userModel = User.builder()
            .firstName(createUserDTO.firstName())
            .lastName(createUserDTO.lastName())
            .birthDate(createUserDTO.birthDate())
            .address(createUserDTO.address())
            .phone(createUserDTO.phone())
            .email(createUserDTO.email())
            .baseSalary(createUserDTO.baseSalary())
            .roleId(1)
            .build();

    private final  CreateUserDTO validUserDTO = CreateUserDTO.builder()
            .firstName("Juan")
            .lastName("Pérez")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("Calle 123")
            .phone("3001234567")
            .email("juan.perez@test.com")
            .identityDocument("100200300")
            .baseSalary(BigDecimal.valueOf(1500000))
            .roleName("ADMINISTRADOR")
            .build();

    private final  UserDTO userDTO = UserDTO.builder()
            .firstName("Juan")
            .lastName("Pérez")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("Calle 123")
            .phone("3001234567")
            .email("juan.perez@test.com")
            .identityDocument("100200300")
            .baseSalary(BigDecimal.valueOf(1500000))
            .roleName("ADMINISTRADOR")
            .build();

    private final UserIdentityDocumentDTO userIdentityDocumentDTO = UserIdentityDocumentDTO.builder()
            .identityDocument("100200300")
            .build();



    @Test
    void shouldRegisterUserSuccessfully() {

        when(validationHandler.validate(any(CreateUserDTO.class))).thenReturn(Mono.just(createUserDTO));
        when(userDTOMapper.toModel(any(CreateUserDTO.class))).thenReturn(userModel);
        when(registerUserUseCase.register(any(User.class), anyString())).thenReturn(Mono.just(userModel));

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createUserDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(userResponse -> {
                    Assertions.assertThat(userResponse.getEmail()).isEqualTo(createUserDTO.email());
                    Assertions.assertThat(userResponse.getFirstName()).isEqualTo(createUserDTO.firstName());
                });
    }



    @Test
    void whenFirstNameIsBlank_thenReturnBadRequest() {

        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("firstName");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("FirstName is required");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().firstName("").build();


        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("firstName: FirstName is required");
    }

    @Test
    void whenLastNameIsBlank_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("lastName");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("LastName is required");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().lastName("").build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("lastName: LastName is required");

    }

    @Test
    void whenBirthDateIsNull_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("birthDate");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("BirthDate is required");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().birthDate(null).build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("birthDate: BirthDate is required");

    }

    @Test
    void whenAddressIsBlank_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("address");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("Address is required");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().birthDate(null).build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("address: Address is required");
    }

    @Test
    void whenPhoneIsBlank_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("phone");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("Phone is required");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().phone("").build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("phone: Phone is required");
    }

    @Test
    void whenEmailIsBlank_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("email");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("Email is required");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().email("").build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("email: Email is required");
    }

    @Test
    void whenEmailIsInvalid_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("email");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("Invalid format email");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().email("invalid-email").build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("email: Invalid format email");
    }


    @Test
    void whenIdentityDocumentIsBlank_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("identityDocument");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("IdentityDocument is requited");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().identityDocument("").build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("identityDocument: IdentityDocument is requited");
    }


    @Test
    void whenBaseSalaryIsNull_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("baseSalary");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("BaseSalary is required");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().baseSalary(null).build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("baseSalary: BaseSalary is required");
    }

    @Test
    void whenBaseSalaryIsZero_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("baseSalary");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("BaseSalary must be greater than 0.");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().baseSalary(BigDecimal.ZERO).build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("baseSalary: BaseSalary must be greater than 0.");
    }

    @Test
    void whenBaseSalaryIsNegative_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("baseSalary");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("BaseSalary must be greater than 0.");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().baseSalary(BigDecimal.valueOf(-1000)).build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("baseSalary: BaseSalary must be greater than 0.");
    }

    @Test
    void whenBaseSalaryExceedsMax_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("baseSalary");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("BaseSalary cannot exceed 15 million.");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));


        CreateUserDTO invalidDTO = validUserDTO.toBuilder().baseSalary(BigDecimal.valueOf(16000000)).build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("baseSalary: BaseSalary cannot exceed 15 million.");
    }

    @Test
    void whenRoleNameIsBlank_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("roleName");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("Role is required");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().roleName("").build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("roleName: Role is required");
    }

    @Test
    void shouldReturnUserSuccessfully() {
        UserIdentityDocumentDTO dto = new UserIdentityDocumentDTO("1234567890");

        when(validationHandler.validate(any(UserIdentityDocumentDTO.class))).thenReturn(Mono.just(dto));
        when(validationUserUseCase.getUserByIdentityDocument(anyString())).thenReturn(Mono.just(userModel));
        when(userDTOMapper.toResponse(any(User.class))).thenReturn(userDTO);

        webTestClient.get()
                .uri("/api/v1/users/identity-document/1234567890")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(response -> {
                    Assertions.assertThat(response.identityDocument()).isEqualTo("100200300");
                    Assertions.assertThat(response.firstName()).isEqualTo("Juan");
                    Assertions.assertThat(response.roleName()).isEqualTo("ADMINISTRADOR");
                });
    }



    @Test
    void whenIdentityDocumentInvalid_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("identityDocument");
        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("Identity document must be 6-10 digits");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        when(validationHandler.validate(any(UserIdentityDocumentDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        webTestClient.get()
                .uri("/api/v1/users/identity-document/123")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("identityDocument: Identity document must be 6-10 digits");
    }


    @Test
    void whenPasswordIsBlank_thenReturnBadRequest() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = PathImpl.createPathFromString("password");

        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("Password is requited");

        Set<ConstraintViolation<?>> violations = Set.of(violation);

        Mockito.when(validationHandler.validate(any(CreateUserDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException("Validation failed", violations)));

        CreateUserDTO invalidDTO = validUserDTO.toBuilder().password("").build();

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("VALIDATION_FAILED")
                .jsonPath("$.message[0]").isEqualTo("assword: Password is requited");
    }

    @Test
    void whenInternalError_thenReturnServerError() {
        UserIdentityDocumentDTO dto = new UserIdentityDocumentDTO("1234567890");

        when(validationHandler.validate(any(UserIdentityDocumentDTO.class))).thenReturn(Mono.just(dto));
        when(validationUserUseCase.getUserByIdentityDocument(anyString()))
                .thenReturn(Mono.error(new RuntimeException("Database connection failed")));

        webTestClient.get()
                .uri("/api/v1/users/identity-document/1234567890")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Database connection failed");
    }
}

