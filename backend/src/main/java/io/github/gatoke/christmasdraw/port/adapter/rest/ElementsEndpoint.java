package io.github.gatoke.christmasdraw.port.adapter.rest;

//@RestController
//@RequestMapping(value = "/elements")
//@RequiredArgsConstructor
class ElementsEndpoint {

//    private final ElementApplicationService elementApplicationService;
//    private final ElementRepository elementRepository;
//    private final SessionRepository sessionRepository;
//
//    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    Mono<Element> addElement(@RequestBody @Valid final AddElementRequest request) {
//        return elementApplicationService.addElement(
//                request.getDescription(), request.getSessionId()
//        );
//    }
//
//    @DeleteMapping(value = "/{elementId}")
//    Mono<Void> removeElement(@PathVariable final String elementId) {//todo session for security?
//        return elementApplicationService.removeElement(elementId);
//    }
//
//    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    Flux<Element> streamElements(@RequestParam final String sessionId) { //todo websocket chyba bezpieczniejszy
////        return Flux.interval(Duration.ofSeconds(1))
////                .flatMap(s -> sessionRepository.validate(sessionId))
////                .flatMap(s -> elementRepository.findAllBy(sessionId))
////                .doOnNext(System.out::println);
////
//        return elementRepository.findAllBy(sessionId)
//                .doOnNext(System.out::println);
//    }
//
//    @Data
//    private static class AddElementRequest {
//        private String description;
//        private String sessionId;
//    }
}
