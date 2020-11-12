package io.github.gatoke.christmasdraw.port.adapter.persistence;

import io.github.gatoke.christmasdraw.domain.Session;
import io.github.gatoke.christmasdraw.domain.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
class SessionMongoRepository implements SessionRepository {

    private final SessionReactiveMongoRepository repository;

    @Override
    public Mono<Session> add(final Session session) {
        return repository.save(session);
    }

    @Override
    public Mono<Session> findOrThrow(final String sessionId) {
        return repository.findById(sessionId)
                .switchIfEmpty(
                        Mono.error(new IllegalArgumentException())
                ); //todo 404
    }

    @Override
    public Mono<Void> validate(final String sessionId) {
        return repository.existsById(sessionId)
                .flatMap(doesExist -> {
                    if (doesExist) {
                        return Mono.empty();
                    }
                    return Mono.error(new IllegalAccessException());//todo
                });
    }

    @Override
    public Mono<Boolean> existsById(final String sessionId) {
        return repository.existsById(sessionId);
    }
}
