package io.github.gatoke.christmasdraw.port.adapter.persistence;

import io.github.gatoke.christmasdraw.domain.Session;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SessionReactiveMongoRepository extends ReactiveMongoRepository<Session, String> {

}
