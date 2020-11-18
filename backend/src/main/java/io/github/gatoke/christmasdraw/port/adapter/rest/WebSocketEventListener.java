package io.github.gatoke.christmasdraw.port.adapter.rest;

import io.github.gatoke.christmasdraw.application.ChannelApplicationService;
import io.github.gatoke.christmasdraw.application.RandomPersonService;
import io.github.gatoke.christmasdraw.domain.Channel;
import io.github.gatoke.christmasdraw.domain.User;
import io.github.gatoke.christmasdraw.domain.event.AllUsersReadyEvent;
import io.github.gatoke.christmasdraw.domain.event.RandomPersonPickedEvent;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChannelApplicationService channelApplicationService;
    private final RandomPersonService randomPersonService;

    @EventListener
    public void handleWebSocketConnectListener(final SessionConnectedEvent event) {
        log.info("New connection!");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event) {
        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final String userId = headerAccessor.getSessionId();
        final String channelId = (String) headerAccessor.getSessionAttributes().get("channelId");

        if (channelId == null) {
            log.info("{} disconnected.", userId);
            return;
        }

        if (userId != null) {
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
        final Channel channel = event.getChannel();
        sendingOperations.convertAndSend("/topic/channel." + channel.getId(), event);


        channel.getConnectedUsers()
                .forEach(user -> {
                    try {
                        final User randomPerson = randomPersonService.pickRandomPersonForUserInChannel(user.getId(), channel.getId());
                        sendingOperations.convertAndSend(
                                "/topic/channel." + channel.getId() + "-" + user.getId(),
                                new RandomPersonPickedEvent(randomPerson.getId(), randomPerson.getName())
                        );
                    } catch (final Exception e) {
                        log.error("Picking random person from channel failed! Cause: {}", e.getMessage());
                    }
                });
    }
}
