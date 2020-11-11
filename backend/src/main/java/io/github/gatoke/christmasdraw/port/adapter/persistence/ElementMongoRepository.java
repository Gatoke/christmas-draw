package io.github.gatoke.christmasdraw.port.adapter.persistence;

import io.github.gatoke.christmasdraw.domain.Element;
import io.github.gatoke.christmasdraw.domain.ElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
class ElementMongoRepository implements ElementRepository {

    private final ElementReactiveMongoRepository repository;

    @Override
    public Mono<Element> add(final Element element) {
        return repository.save(element);
    }

    @Override
    public Flux<Element> findAllBy(final String sessionId) {
        return repository.findAllBySessionId(sessionId);
    }

    @Override
    public Mono<Void> remove(final String elementId) {
        return repository.deleteById(elementId);
    }
}
