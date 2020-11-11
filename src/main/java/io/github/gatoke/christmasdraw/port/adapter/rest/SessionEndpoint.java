package io.github.gatoke.christmasdraw.port.adapter.rest;

import io.github.gatoke.christmasdraw.application.SessionApplicationService;
import io.github.gatoke.christmasdraw.domain.Session;
import io.github.gatoke.christmasdraw.domain.SessionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@RestController
@RequestMapping(value = "/sessions")
@RequiredArgsConstructor
class SessionEndpoint {

    private final SessionApplicationService sessionApplicationService;
    private final SessionRepository sessionRepository;

    @PostMapping
    public Mono<Session> createSession(@RequestBody @Valid final CreateSessionRequest request) {
        return sessionApplicationService.createSession(request.getSessionName());
    }

    @GetMapping
    public Mono<Session> getSession(@RequestParam final String sessionId) {
        return sessionRepository.findOrThrow(sessionId);
    }

    @Data
    private static class CreateSessionRequest {

        @Size(max = 255)
        private String sessionName;
    }
}
