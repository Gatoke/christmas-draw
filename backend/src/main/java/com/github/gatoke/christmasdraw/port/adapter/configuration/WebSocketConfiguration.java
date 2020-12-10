package com.github.gatoke.christmasdraw.port.adapter.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

import static java.util.UUID.randomUUID;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/events")
                .setAllowedOrigins("127.0.0.1", "http://localhost:3000", "http://192.168.0.192:3000", "http://localhost:8080")
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

    private static class CustomHandshakeHandler extends DefaultHandshakeHandler {

        @Override
        protected Principal determineUser(final ServerHttpRequest request, final WebSocketHandler wsHandler, final Map<String, Object> attributes) {
            return new StompPrincipal(randomUUID().toString());
        }

        private static class StompPrincipal implements Principal {

            private String name;

            StompPrincipal(final String name) {
                this.name = name;
            }

            @Override
            public String getName() {
                return name;
            }
        }
    }

}
