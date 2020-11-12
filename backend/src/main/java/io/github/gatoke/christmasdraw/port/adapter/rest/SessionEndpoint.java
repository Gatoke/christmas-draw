package io.github.gatoke.christmasdraw.port.adapter.rest;

//@RestController
//@RequestMapping(value = "/sessions")
//@RequiredArgsConstructor
class SessionEndpoint {

//    private final SessionApplicationService sessionApplicationService;
//    private final SessionRepository sessionRepository;
//
//    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
//
////    @PostMapping
////    public Mono<Session> createSession(@RequestBody @Valid final CreateSessionRequest request) {
////        return sessionApplicationService.createSession(request.getSessionName());
////    }
//
//    @GetMapping
//    SseEmitter session(@RequestParam final String sessionId) {
//        final SseEmitter sseEmitter = new SseEmitter();
//        emitters.add(sseEmitter);
//        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
//        return sseEmitter;
//    }
//
////    @PostMapping
////    public void createSession(@RequestBody @Valid final CreateSessionRequest request) throws IOException {
////        for (final SseEmitter emitter : emitters) {
////            emitter.send(SseEmitter.event().name("spring").data(request));
////        }
////        return sessionApplicationService.createSession(request.getSessionName());
////    }
//
//    @PostMapping
//    public void postEvent(final String message) throws IOException {
//        for (final SseEmitter emitter : emitters) {
//            emitter.send(SseEmitter.event().name("spring").data(message));
//        }
//    }
//
////    @GetMapping(value = "/{sessionId}")
////    Mono<Session> getSession(@PathVariable final String sessionId) {
////        return sessionRepository.findOrThrow(sessionId);
////    }
//
//    @Data
//    private static class CreateSessionRequest {
//
//        @Size(max = 255)
//        private String sessionName;
//    }
}
