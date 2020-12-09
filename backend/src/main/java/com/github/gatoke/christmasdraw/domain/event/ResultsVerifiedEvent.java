package com.github.gatoke.christmasdraw.domain.event;

import com.github.gatoke.christmasdraw.domain.Channel;
import lombok.Data;

import java.util.List;

@Data
public class ResultsVerifiedEvent implements DomainEvent {

    private EventType eventType = EventType.RESULTS_VERIFIED;
    private Channel channel;
    private List<String> peopleResults;

    public ResultsVerifiedEvent(final Channel channel, final List<String> peopleResults) {
        this.channel = channel;
        this.peopleResults = peopleResults;
    }
}
