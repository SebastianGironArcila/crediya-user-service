package co.com.crediya.r2dbc.user;

import co.com.crediya.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDataRepositoryAdapterTest {

    @InjectMocks
    private UserDataRepositoryAdapter adapter;

    @Mock
    private UserDataRepository repository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private TransactionalOperator transactionalOperator;

    private  final UserData userData = UserData.builder()
            .id(1L)
            .firstName("Dahiana")
            .lastName("Reyes")
            .birthDate(java.time.LocalDate.of(1990, 8, 23))
            .address("Calle 123")
            .phone("3001234567")
            .email("dahiana@example.com")
            .baseSalary(BigDecimal.valueOf(2000.0))
            .roleId(1)
            .build();

    private  final User user = User.builder()
            .firstName("Dahiana")
            .lastName("Reyes")
            .birthDate(java.time.LocalDate.of(1990, 8, 23))
            .address("Calle 123")
            .phone("3001234567")
            .email("dahiana@example.com")
            .baseSalary(BigDecimal.valueOf(2000.0))
            .roleId(1)
            .build();


    @Test
    void shouldSaveUser() {
        when(mapper.map(userData, User.class)).thenReturn(user);
        when(mapper.map(user, UserData.class)).thenReturn(userData);
        when(repository.save(userData)).thenReturn(Mono.just(userData));
        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

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

    @Test
    void shouldFindUserByIdentityDocument() {
        String identityDocument = "123456789";

        when(repository.findByIdentityDocument(identityDocument)).thenReturn(Mono.just(userData));
        when(mapper.map(userData, User.class)).thenReturn(user);

        Mono<User> result = adapter.findByIdentityDocument(identityDocument);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByIdentityDocument() {
        String identityDocument = "999999999";

        when(repository.findByIdentityDocument(identityDocument)).thenReturn(Mono.empty());

        Mono<User> result = adapter.findByIdentityDocument(identityDocument);

        StepVerifier.create(result)
                .verifyComplete();
    }

}
