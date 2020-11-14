package io.github.gatoke.christmasdraw.domain.event;

import io.github.gatoke.christmasdraw.domain.Channel;
import lombok.Data;

@Data
public class AllUsersReadyEvent implements DomainEvent {

    private EventType eventType = EventType.ALL_USERS_READY;
    private Channel channel;

    public AllUsersReadyEvent(final Channel channel) {
        this.channel = channel;
    }
}
