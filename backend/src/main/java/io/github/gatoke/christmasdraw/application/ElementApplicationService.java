package io.github.gatoke.christmasdraw.application;

import io.github.gatoke.christmasdraw.domain.Element;
import io.github.gatoke.christmasdraw.domain.ElementRepository;
import io.github.gatoke.christmasdraw.domain.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ElementApplicationService {

    private final SessionRepository sessionRepository;
    private final ElementRepository elementRepository;

    public Mono<Element> addElement(final String description, final String sessionId) {
        return sessionRepository.existsById(sessionId)
                .flatMap(exists -> exists
                        ? elementRepository.add(new Element(description, sessionId))
                        : Mono.error(new IllegalArgumentException())//todo 404
                );
    }

    public Mono<Void> removeElement(final String elementId) {
        return elementRepository.remove(elementId);
    }
}
