package io.github.gatoke.christmasdraw.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.time.Clock.systemUTC;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@Getter
@Document(collection = "session")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Channel {

    @Id
    private String id;

    private String name;

    private boolean isClosed;

    private Set<User> connectedUsers;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    public Channel(final String sessionName) {
        this.id = randomUUID().toString();
        this.name = sessionName;
        this.isClosed = false;
        this.connectedUsers = new HashSet<>();
        this.createdAt = now(systemUTC());
        this.lastUpdatedAt = now(systemUTC());
    }

    public void addUser(final String username) {
        this.connectedUsers.add(new User(username));
        this.lastUpdatedAt = now(systemUTC());
    }

    public void removeUser(final String username) {
        this.connectedUsers.removeIf(user -> user.getName().equals(username));
        this.lastUpdatedAt = now(systemUTC());
    }

    public void switchUserReadyStatus(final String username) {
        this.connectedUsers.stream()
                .filter(user -> user.getName().equals(username))
                .forEach(User::switchReadyStatus);
    }
}
