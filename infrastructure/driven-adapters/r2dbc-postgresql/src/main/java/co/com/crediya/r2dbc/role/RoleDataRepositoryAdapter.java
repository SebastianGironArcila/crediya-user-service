package co.com.crediya.r2dbc.role;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class RoleDataRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleData,
        Integer,
        RoleDataRepository
        > implements RoleRepository {

    private final RoleDataRepository repository;

    public RoleDataRepositoryAdapter(RoleDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, Role.class));
        this.repository = repository;
    }


    @Override
    public Mono<Role> findByName(String name) {
        return repository.findByName(name)
                .map(entity -> mapper.map(entity, Role.class))
                .doOnNext(role -> log.debug("Role found: {} with ID: {}", name, role.getId()))
                .doOnError(error -> log.error("Error finding role {}: {}", name, error.getMessage()));
    }
}
