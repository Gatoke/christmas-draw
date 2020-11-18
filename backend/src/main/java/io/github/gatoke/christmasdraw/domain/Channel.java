package io.github.gatoke.christmasdraw.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.time.Clock.systemUTC;
import static java.time.LocalDateTime.now;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.RandomStringUtils.random;

@Getter
@Document(collection = "session")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Channel {

    @Id
    private String id;

    private String name;

    private Boolean isClosed;

    private Set<User> connectedUsers;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    public Channel(final String channelName) {
        this.id = random(10, 0, 0, true, true, null, new SecureRandom());
        this.name = channelName;
        this.isClosed = false;
        this.connectedUsers = new HashSet<>();
        this.createdAt = now(systemUTC());
        this.lastUpdatedAt = now(systemUTC());
    }

    public void addUser(final String userId, final String username) {
        this.connectedUsers.add(new User(userId, username, this.id));
        this.lastUpdatedAt = now(systemUTC());
    }

    public void removeUser(final String userId) {
        this.connectedUsers.removeIf(user -> user.getId().equals(userId));
        this.lastUpdatedAt = now(systemUTC());
    }

    public void switchUserReadyStatus(final String userId) {
        this.connectedUsers.stream()
                .filter(user -> user.getId().equals(userId))
                .forEach(User::switchReadyStatus);
        this.lastUpdatedAt = now(systemUTC());
    }

    public boolean areAllUsersReady() {
        return this.connectedUsers.stream().allMatch(User::getIsReady);
    }

    public Set<User> getNotPickedUsersExcept(final String userId) {
        return this.connectedUsers.stream()
                .filter(not(user -> user.getId().equals(userId)))
                .filter(not(User::getIsChosen))
                .collect(toSet());
    }

    public User getUser(final String userId) {
        return this.connectedUsers.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No user of id: %s in channel: %s", userId, this.id)));
    }

    public boolean hasEveryonePicked() {
        return this.connectedUsers.stream().allMatch(user -> user.getChosenUserId() != null);
    }

    public void close() {
        this.isClosed = true;
    }
}
