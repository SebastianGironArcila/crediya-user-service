package co.com.crediya.r2dbc.role;

import co.com.crediya.model.role.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleDataRepositoryAdapterTest {

    @InjectMocks
    private RoleDataRepositoryAdapter adapter;

    @Mock
    private RoleDataRepository repository;

    @Mock
    private ObjectMapper mapper;

    private final RoleData roleData = RoleData.builder()
            .id(1)
            .name("ADMINISTRADOR")
            .description("Rol con permisos de administración")
            .build();

    private final Role role = Role.builder()
            .id(1)
            .name("ADMINISTRADOR")
            .description("Rol con permisos de administración")
            .build();

    @Test
    void shouldFindRoleByName() {
        when(repository.findByName("ADMINISTRADOR")).thenReturn(Mono.just(roleData));
        when(mapper.map(roleData, Role.class)).thenReturn(role);

        Mono<Role> result = adapter.findByName("ADMINISTRADOR");

        StepVerifier.create(result)
                .expectNext(role)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenRoleNotFound() {
        when(repository.findByName("ROL_INEXISTENTE")).thenReturn(Mono.empty());

        Mono<Role> result = adapter.findByName("ROL_INEXISTENTE");

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void shouldSaveRole() {
        when(mapper.map(role, RoleData.class)).thenReturn(roleData);
        when(mapper.map(roleData, Role.class)).thenReturn(role);
        when(repository.save(roleData)).thenReturn(Mono.just(roleData));

        // Ejecutar el método save (que es heredado)
        Mono<Role> result = adapter.save(role);

        // Verificar el resultado
        StepVerifier.create(result)
                .expectNext(role)
                .verifyComplete();
    }

    @Test
    void shouldFindRoleById() {
        when(repository.findById(1)).thenReturn(Mono.just(roleData));
        when(mapper.map(roleData, Role.class)).thenReturn(role);

        Mono<Role> result = adapter.findById(1);

        StepVerifier.create(result)
                .expectNext(role)
                .verifyComplete();
    }




}