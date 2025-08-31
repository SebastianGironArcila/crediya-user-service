package co.com.crediya.r2dbc.user;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class UserDataRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserData,
        Long,
        UserDataRepository
        > implements UserRepository {

    private final UserDataRepository repository;
    private final TransactionalOperator  transactionalOperator;

    public UserDataRepositoryAdapter(UserDataRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
        this.repository = repository;
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<User> save(User user) {
        return super.save(user)
                .as(transactionalOperator::transactional)
                .doOnSuccess(savedUser -> log.debug("User saved successfully - ID: {}", savedUser.getId()))
                .doOnError(error -> log.error("Error saving user: {}", error.getMessage()));
    }


    @Override
    public Mono<Boolean> existsByEmail(String email) {
        log.debug("Checking email existence: {}", email);
        return repository.existsByEmail(email)
                .doOnNext(exists -> log.debug("Email {} exists: {}", email, exists))
                .doOnError(error -> log.error("Error checking email existence: {}", error.getMessage()));
    }

    @Override
    public Mono<User> findByIdentityDocument(String identityDocument) {
        log.debug("Finding user by identity document: {}", identityDocument);
        return repository.findByIdentityDocument(identityDocument)
                .map(this::toEntity)
                .doOnNext(user -> log.debug("User found: {}", user.getId()))
                .doOnError(error -> log.error("Error finding user by identity document: {}", error.getMessage()));
    }
}
