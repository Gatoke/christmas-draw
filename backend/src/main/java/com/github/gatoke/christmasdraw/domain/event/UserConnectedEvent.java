package com.github.gatoke.christmasdraw.domain.event;

import com.github.gatoke.christmasdraw.domain.Channel;
import lombok.Data;

@Data
public class UserConnectedEvent implements DomainEvent {

    private EventType eventType = EventType.USER_CONNECTED;
    private Channel channel;

    public UserConnectedEvent(final Channel channel) {
        this.channel = channel;
    }
}
