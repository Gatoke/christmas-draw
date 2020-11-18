package io.github.gatoke.christmasdraw.port.adapter.configuration;

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
        registry.addEndpoint("/messages")
                .setAllowedOrigins("*")//todo
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
//        final long heartbeatServer = 10000; // 10 seconds
//        final long heartbeatClient = 10000; // 10 seconds
//
//        final ThreadPoolTaskScheduler ts = new ThreadPoolTaskScheduler();
//        ts.setPoolSize(2);
//        ts.setThreadNamePrefix("wss-heartbeat-thread-");
//        ts.initialize();


        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue");
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
