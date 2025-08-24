package co.com.crediya.r2dbc;

import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.user.UserData;
import co.com.crediya.r2dbc.user.UserDataRepository;
import co.com.crediya.r2dbc.user.UserDataRepositoryAdapter;
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
class UserDataRepositoryAdapterTest {

    @InjectMocks
    private UserDataRepositoryAdapter adapter;

    @Mock
    private UserDataRepository repository;

    @Mock
    private ObjectMapper mapper;

    private  final UserData userData = UserData.builder()
            .id(1L)
            .firstName("Dahiana")
            .lastName("Reyes")
            .birthDate(java.time.LocalDate.of(1990, 8, 23))
            .address("Calle 123")
            .phone("3001234567")
            .email("dahiana@example.com")
            .baseSalary(2000.0)
            .build();

    private  final User user = User.builder()
            .firstName("Dahiana")
            .lastName("Reyes")
            .birthDate(java.time.LocalDate.of(1990, 8, 23))
            .address("Calle 123")
            .phone("3001234567")
            .email("dahiana@example.com")
            .baseSalary(2000.0)
            .build();


    @Test
    void shouldSaveUser() {
        when(mapper.map(userData, User.class)).thenReturn(user);
        when(mapper.map(user, UserData.class)).thenReturn(userData);
        when(repository.save(userData)).thenReturn(Mono.just(userData));

        Mono<User> result = adapter.save(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustCheckExistsByEmail() {
        when(repository.existsByEmail("dahiana@example.com")).thenReturn(Mono.just(true));

        Mono<Boolean> result = adapter.existsByEmail("dahiana@example.com");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }
}
