package co.com.crediya.r2dbc.role;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RoleDataRepository extends ReactiveCrudRepository<RoleData, Integer>, ReactiveQueryByExampleExecutor<RoleData> {
    Mono<RoleData> findByName(String name);
}
