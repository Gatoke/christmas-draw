package io.github.gatoke.christmasdraw.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

import static java.time.Clock.systemUTC;
import static java.time.OffsetDateTime.now;
import static java.util.UUID.randomUUID;

@Getter
@Document
public class Session {

    @Id
    private String id;

    private String name;

    private OffsetDateTime createdAt;

    public Session(final String sessionName) {
        this.id = randomUUID().toString();
        this.name = sessionName;
        this.createdAt = now(systemUTC());
    }
}
