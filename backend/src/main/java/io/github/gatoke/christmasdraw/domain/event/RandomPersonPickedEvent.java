package io.github.gatoke.christmasdraw.domain.event;

import lombok.Data;

import static io.github.gatoke.christmasdraw.domain.event.EventType.RANDOM_PERSON_PICKED;

@Data
public class RandomPersonPickedEvent implements DomainEvent {

    private EventType eventType = RANDOM_PERSON_PICKED;
    private String userId;
    private String username;

    public RandomPersonPickedEvent(final String userId, final String username) {
        this.userId = userId;
        this.username = username;
    }
}
