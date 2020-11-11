package io.github.gatoke.christmasdraw.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ElementRepository {

    Mono<Element> add(final Element element);

    Flux<Element> findAllBy(final String sessionId);

    Mono<Void> remove(final String elementId);
}
