package io.github.gatoke.christmasdraw.port.adapter.persistence;

import io.github.gatoke.christmasdraw.domain.Element;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ElementReactiveMongoRepository extends ReactiveMongoRepository<Element, String> {

    Flux<Element> findAllBySessionId(final String sessionId);
}
