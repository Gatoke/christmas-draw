package com.github.gatoke.christmasdraw.domain.event;

import lombok.Data;

@Data
public class RandomPersonPickedEvent implements DomainEvent {

    private EventType eventType = EventType.RANDOM_PERSON_PICKED;
    private String username;

    public RandomPersonPickedEvent(final String username) {
        this.username = username;
    }
}
