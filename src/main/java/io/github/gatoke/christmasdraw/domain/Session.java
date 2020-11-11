package io.github.gatoke.christmasdraw.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Clock;
import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;

@Getter
@Document
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Session {

    @Id
    private String id;

    private String name;

    //    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    public Session(final String sessionName) {
        this.id = randomUUID().toString();
        this.name = sessionName;
        this.createdAt = LocalDateTime.now(Clock.systemUTC());
    }
}
