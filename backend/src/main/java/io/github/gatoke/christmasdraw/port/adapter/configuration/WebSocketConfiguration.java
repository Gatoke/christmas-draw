package io.github.gatoke.christmasdraw.port.adapter.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/messages")
                .setAllowedOrigins("*")//todo
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}

//
//
//    @Override
//    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
//        registry.addHandler(new QuestionHandler(), "/questions")
//                .addInterceptors(new HttpHandshakeInterceptor())
//                .setAllowedOrigins("*"); //todo set allowed origins
//    }
//
//static class QuestionHandler extends TextWebSocketHandler {
//
//    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
//
//    @Override
//    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
//        sessions.add(session);
//    }
//
//    @Override
//    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
//        sessions.forEach(webSocketSession -> {
//            try {
//                webSocketSession.sendMessage(message);
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    @Override
//    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
//        sessions.remove(session);
//    }
//}
