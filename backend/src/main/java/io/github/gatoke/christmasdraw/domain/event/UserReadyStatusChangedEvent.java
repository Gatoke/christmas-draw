package io.github.gatoke.christmasdraw.domain.event;

import io.github.gatoke.christmasdraw.domain.Channel;
import lombok.Data;

@Data
public class UserReadyStatusChangedEvent {

    private EventType eventType = EventType.USER_READY_STATUS_CHANGED;
    private Channel channel;

    public UserReadyStatusChangedEvent(final Channel channel) {
        this.channel = channel;
    }
}
