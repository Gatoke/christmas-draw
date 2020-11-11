package io.github.gatoke.christmasdraw.port.adapter.rest;

import io.github.gatoke.christmasdraw.application.ElementApplicationService;
import io.github.gatoke.christmasdraw.domain.Element;
import io.github.gatoke.christmasdraw.domain.ElementRepository;
import io.github.gatoke.christmasdraw.domain.SessionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/elements")
@RequiredArgsConstructor
class ElementsEndpoint {

    private final ElementApplicationService elementApplicationService;
    private final ElementRepository elementRepository;
    private final SessionRepository sessionRepository;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<Element> addElement(@RequestBody @Valid final AddElementRequest request) {
        return elementApplicationService.addElement(
                request.getDescription(), request.getSessionId()
        );
    }

    @DeleteMapping(value = "/{elementId}")
    Mono<Void> removeElement(@PathVariable final String elementId) {//todo session for security?
        return elementApplicationService.removeElement(elementId);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Element> streamElements(@RequestParam final String sessionId) {
        return elementRepository.findAllBy(sessionId)
                .doOnNext(System.out::println);
    }

    @Data
    private static class AddElementRequest {
        private String description;
        private String sessionId;
    }
}
