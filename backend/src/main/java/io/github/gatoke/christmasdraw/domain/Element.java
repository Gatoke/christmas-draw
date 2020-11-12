package io.github.gatoke.christmasdraw.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static java.util.UUID.randomUUID;

@Getter
@Document(collection = "element")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Element {

    @Id
    private String id;

    private String description;

    private String sessionId;

    public Element(final String description, final String sessionId) {
        this.id = randomUUID().toString();
        this.description = description;
        this.sessionId = sessionId;
    }
}