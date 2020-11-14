package io.github.gatoke.christmasdraw.port.adapter.rest;

import io.github.gatoke.christmasdraw.application.ChannelApplicationService;
import io.github.gatoke.christmasdraw.domain.Channel;
import io.github.gatoke.christmasdraw.domain.event.UserConnectedEvent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Map;

@Controller
@RequiredArgsConstructor
class WebSocketController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChannelApplicationService channelService;

    @MessageMapping("/chat.newUser")
    void newUser(@Payload @Valid final ConnectUserRequest request,
                 final SimpMessageHeaderAccessor headerAccessor) {
        final Channel channel = channelService.addUserToChannel(request.getUsername(), request.getChannelId());

        final Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        sessionAttributes.put("username", request.getUsername());
        sessionAttributes.put("channelId", channel.getId());

        sendingOperations.convertAndSend(
                "/topic/channel." + channel.getId(),
                new UserConnectedEvent(channel)
        );
    }

    @PostMapping("/createChannel")
    ResponseEntity<Channel> createChannel(@RequestBody @Valid final CreateChannelRequest request) {
        final Channel channel = channelService.createChannel(request.getChannelName());
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @Data
    private static class CreateChannelRequest {

        @NotBlank
        @Size(max = 255)
        private String channelName;
    }

    @Data
    private static class ConnectUserRequest {

        @NotBlank
        @Size(max = 255)
        private String channelId;

        @Size(max = 255)
        private String username;
    }
}
