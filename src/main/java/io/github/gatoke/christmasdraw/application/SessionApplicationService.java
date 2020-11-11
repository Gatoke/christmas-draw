package io.github.gatoke.christmasdraw.application;

import io.github.gatoke.christmasdraw.domain.Session;
import io.github.gatoke.christmasdraw.domain.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SessionApplicationService {

    private final SessionRepository sessionRepository;

    public Mono<Session> createSession(final String sessionName) {
        final Session session = new Session(sessionName);
        return sessionRepository.add(session);
    }
}
