package co.com.crediya.usecase.user;

import co.com.crediya.model.common.exception.BusinessException;
import co.com.crediya.model.common.gateways.PasswordEnconderService;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.model.role.gateways.RoleRepository;
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

class RegisterUserUseCaseTest {

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEnconderService passwordEnconderService; // 👈 mock faltante

    private User validUser;
    private Role adminRole;
    private final String roleName = "ADMINISTRADOR";

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
                .password("rawPassword") // 👈 agrega password inicial
                .baseSalary(BigDecimal.valueOf(2000.0))
                .roleId(1)
                .build();

        adminRole = Role.builder()
                .id(1)
                .name("ADMINISTRADOR")
                .description("Rol con permisos de administración")
                .build();
    }

    @Test
    void mustFailWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(registerUserUseCase.register(validUser, roleName))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.EMAIL_ALREADY_REGISTERED.name()))
                .verify();
    }

    @Test
    void mustFailWhenRoleNotFound() {
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.findByName(roleName)).thenReturn(Mono.empty());

        StepVerifier.create(registerUserUseCase.register(validUser, roleName))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getCode().equals(BusinessException.Type.ROLE_NOT_FOUND.name()))
                .verify();
    }

    @Test
    void mustRegisterSuccessfully() {
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.findByName(roleName)).thenReturn(Mono.just(adminRole));
        when(passwordEnconderService.encode("rawPassword")).thenReturn("encodedPassword"); // 👈 simulamos encriptación

        User userWithRole = validUser.toBuilder()
                .roleId(adminRole.getId())
                .password("encodedPassword") // 👈 aseguramos encoded
                .build();

        when(userRepository.save(userWithRole)).thenReturn(Mono.just(userWithRole));

        StepVerifier.create(registerUserUseCase.register(validUser, roleName))
                .expectNextMatches(savedUser ->
                        savedUser.equals(userWithRole) &&
                                savedUser.getRoleId().equals(adminRole.getId()) &&
                                savedUser.getPassword().equals("encodedPassword")
                )
                .verifyComplete();
    }

    @Test
    void mustSetRoleIdBeforeSaving() {
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.findByName(roleName)).thenReturn(Mono.just(adminRole));
        when(passwordEnconderService.encode("rawPassword")).thenReturn("encodedPassword");

        User expectedUser = validUser.toBuilder()
                .roleId(adminRole.getId())
                .password("encodedPassword")
                .build();

        when(userRepository.save(expectedUser)).thenReturn(Mono.just(expectedUser));

        StepVerifier.create(registerUserUseCase.register(validUser, roleName))
                .expectNextMatches(user ->
                        user.getRoleId() != null &&
                                user.getRoleId().equals(adminRole.getId()) &&
                                user.getPassword().equals("encodedPassword")
                )
                .verifyComplete();
    }

    @Test
    void mustHandleDifferentRoles() {
        String differentRoleName = "FARMER";
        Role userRole = Role.builder()
                .id(2)
                .name("FARMER")
                .description("farmer description")
                .build();

        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.findByName(differentRoleName)).thenReturn(Mono.just(userRole));
        when(passwordEnconderService.encode("rawPassword")).thenReturn("encodedPassword");

        User userWithUserRole = validUser.toBuilder()
                .roleId(userRole.getId())
                .password("encodedPassword")
                .build();

        when(userRepository.save(userWithUserRole)).thenReturn(Mono.just(userWithUserRole));

        StepVerifier.create(registerUserUseCase.register(validUser, differentRoleName))
                .expectNextMatches(savedUser ->
                        savedUser.getRoleId().equals(userRole.getId()) &&
                                savedUser.getPassword().equals("encodedPassword")
                )
                .verifyComplete();
    }
}
