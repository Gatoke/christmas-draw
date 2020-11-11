package io.github.gatoke.christmasdraw.domain;

import reactor.core.publisher.Mono;

public interface SessionRepository {

    Mono<Session> add(final Session session);

    Mono<Session> findOrThrow(final String sessionId);

    Mono<Boolean> existsById(final String sessionId);
}
