package com.github.gatoke.christmasdraw.domain.event;

import com.github.gatoke.christmasdraw.domain.Channel;
import lombok.Data;

@Data
public class UserReadyStatusChangedEvent implements DomainEvent {

    private EventType eventType = EventType.USER_READY_STATUS_CHANGED;
    private Channel channel;

    public UserReadyStatusChangedEvent(final Channel channel) {
        this.channel = channel;
    }
}
