package io.github.gatoke.christmasdraw.domain.event;

import io.github.gatoke.christmasdraw.domain.Channel;
import lombok.Data;

@Data
public class UserConnectedEvent {

    private EventType eventType = EventType.USER_CONNECTED;
    private Channel channel;

    public UserConnectedEvent(final Channel channel) {
        this.channel = channel;
    }
}
