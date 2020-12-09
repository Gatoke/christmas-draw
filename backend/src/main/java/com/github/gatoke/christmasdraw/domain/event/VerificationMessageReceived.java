package com.github.gatoke.christmasdraw.domain.event;

import com.github.gatoke.christmasdraw.domain.Channel;
import lombok.Data;

@Data
public class VerificationMessageReceived implements DomainEvent {

    private EventType eventType = EventType.VERIFICATION_MESSAGE_RECEIVED;
    private Channel channel;
    private String userId;

    public VerificationMessageReceived(final Channel channel, final String userId) {
        this.channel = channel;
        this.userId = userId;
    }
}
