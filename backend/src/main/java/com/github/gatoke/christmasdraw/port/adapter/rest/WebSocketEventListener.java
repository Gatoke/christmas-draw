package com.github.gatoke.christmasdraw.port.adapter.rest;

import com.github.gatoke.christmasdraw.application.ChannelApplicationService;
import com.github.gatoke.christmasdraw.application.DrawApplicationService;
import com.github.gatoke.christmasdraw.application.VerifyApplicationService;
import com.github.gatoke.christmasdraw.domain.Channel;
import com.github.gatoke.christmasdraw.domain.DrawResult;
import com.github.gatoke.christmasdraw.domain.event.AllUsersReadyEvent;
import com.github.gatoke.christmasdraw.domain.event.RandomPersonPickedEvent;
import com.github.gatoke.christmasdraw.domain.event.UserDisconnectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChannelApplicationService channelApplicationService;
    private final VerifyApplicationService verifyApplicationService;
    private final DrawApplicationService drawApplicationService;

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
        performDraw(event.getChannel());
    }

    private void performDraw(final Channel channel) {
        final Set<DrawResult> drawResults = drawApplicationService.performDraw(channel.getId());
        drawResults.forEach(result -> sendingOperations.convertAndSend(
                "/topic/channel." + channel.getId() + "-" + result.getPerformerId(),
                new RandomPersonPickedEvent(result.getResultDisplayName())
        ));
        channelApplicationService.closeChannel(channel.getId());
        verifyApplicationService.createVerifyForChannel(channel.getId(), channel.getConnectedUsers().size());
    }
}
