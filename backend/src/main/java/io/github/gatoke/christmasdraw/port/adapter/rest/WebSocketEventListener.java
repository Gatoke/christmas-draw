package io.github.gatoke.christmasdraw.port.adapter.rest;

import io.github.gatoke.christmasdraw.application.ChannelApplicationService;
import io.github.gatoke.christmasdraw.domain.Channel;
import io.github.gatoke.christmasdraw.domain.event.AllUsersReadyEvent;
import io.github.gatoke.christmasdraw.domain.event.UserDisconnectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChannelApplicationService channelApplicationService;

    @EventListener
    public void handleWebSocketConnectListener(final SessionConnectedEvent event) {
        log.info("New connection!");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event) {
        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final String userId = getUserIdOrNull(headerAccessor.getUser());
        final String channelId = (String) headerAccessor.getSessionAttributes().get("channelId");

        if (userId != null && channelId != null) {
            final Channel channel = channelApplicationService.removeUserFromChannel(userId, channelId);
            sendingOperations.convertAndSend(
                    "/topic/channel." + channelId,
                    new UserDisconnectedEvent(channel)
            );
        }
        log.info("{} disconnected.", userId);
    }

    @Async
    @EventListener
    public void handleAllUsersReadyEvent(final AllUsersReadyEvent event) {
        sendingOperations.convertAndSend("/topic/channel." + event.getChannel().getId(), event);

        //todo shuffle and draw
        //        sendingOperations.convertAndSend(
//                "/topic/channel." + channel.getId() + "-" + headerAccessor.getSessionId(),
//                new AllUsersReadyEvent(channel)
//        );
    }

    private String getUserIdOrNull(final Principal user) {
        if (user == null) {
            return null;
        }
        return user.getName();
    }
}
