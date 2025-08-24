package co.com.crediya.r2dbc.user;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserDataRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserData,
        Long,
        UserDataRepository
        > implements UserRepository {

    private final UserDataRepository repository;

    public UserDataRepositoryAdapter(UserDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
        this.repository = repository;
    }

    @Override
    public Mono<User> save(User user) {
        return super.save(user);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
