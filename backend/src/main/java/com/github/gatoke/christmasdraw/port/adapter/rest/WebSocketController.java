package com.github.gatoke.christmasdraw.port.adapter.rest;

import com.github.gatoke.christmasdraw.application.ChannelApplicationService;
import com.github.gatoke.christmasdraw.application.VerifyApplicationService;
import com.github.gatoke.christmasdraw.domain.Channel;
import com.github.gatoke.christmasdraw.domain.ChannelRepository;
import com.github.gatoke.christmasdraw.domain.Verify;
import com.github.gatoke.christmasdraw.domain.event.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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

@Controller
@RequiredArgsConstructor
class WebSocketController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChannelApplicationService channelService;
    private final ApplicationEventPublisher internalEventPublisher;
    private final VerifyApplicationService verifyApplicationService;
    private final ChannelRepository channelRepository;

    @MessageMapping("/chat.newUser")
    void newUser(@Payload @Valid final ConnectUserRequest request,
                 final SimpMessageHeaderAccessor headerAccessor) {

        final String userId = headerAccessor.getSessionId();

        final Channel channel = channelService.addUserToChannel(userId, request.getUsername(), request.getChannelId());
        headerAccessor.getSessionAttributes().put("channelId", channel.getId());

        sendingOperations.convertAndSend(
                "/topic/channel." + channel.getId(),
                new UserConnectedEvent(channel)
        );
    }

    @MessageMapping("/chat.switchReadyStatus")
    void setReadyStatus(@Payload final SwitchReadyStatusRequest request, final SimpMessageHeaderAccessor headerAccessor) {
        final String userId = headerAccessor.getSessionId();
        final Channel channel = channelService.switchUserReadyStatus(userId, request.getChannelId());
        sendingOperations.convertAndSend(
                "/topic/channel." + channel.getId(),
                new UserReadyStatusChangedEvent(channel)
        );

        if (channel.areAllUsersReady()) {
            internalEventPublisher.publishEvent(new AllUsersReadyEvent(channel, headerAccessor.getSessionId()));
        }
    }

    @MessageMapping(value = "/chat.verifyMessage")
    void sendVerifyMessage(@Payload final SendVerifyMessageRequest request, final SimpMessageHeaderAccessor headerAccessor) {
        final Channel channel = channelRepository.findOrThrow(request.getChannelId());

        final String userId = headerAccessor.getSessionId();
        final Verify verify = verifyApplicationService.sendMessageToVerify(request.getChannelId(), userId, request.getMessage());

        sendingOperations.convertAndSend(
                "/topic/channel." + request.getChannelId(),
                new VerificationMessageReceived(channel, userId)
        );

        if (verify.isVerified()) {
            sendingOperations.convertAndSend(
                    "/topic/channel." + channel.getId(),
                    new ResultsVerifiedEvent(channel, verify.getMessages())
            );
        }
    }

    @PostMapping(value = "/createChannel")
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

        @NotBlank
        @Size(max = 255)
        private String username;
    }

    @Data
    private static class SwitchReadyStatusRequest {

        @NotBlank
        @Size(max = 255)
        private String channelId;
    }

    @Data
    private static class SendVerifyMessageRequest {

        @NotBlank
        @Size(max = 255)
        private String channelId;

        @NotBlank
        @Size(max = 255)
        private String message;
    }
}
