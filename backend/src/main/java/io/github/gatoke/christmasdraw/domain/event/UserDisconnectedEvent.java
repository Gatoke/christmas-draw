package io.github.gatoke.christmasdraw.domain.event;

import io.github.gatoke.christmasdraw.domain.Channel;
import lombok.Data;

@Data
public class UserDisconnectedEvent implements DomainEvent {

    private EventType eventType = EventType.USER_DISCONNECTED;
    private Channel channel;

    public UserDisconnectedEvent(final Channel channel) {
        this.channel = channel;
    }
}
