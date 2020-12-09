package com.github.gatoke.christmasdraw.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.Clock.systemUTC;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.RandomStringUtils.random;

@Getter
@Document
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Channel {

    @Id
    private String id;

    private String name;

    private Boolean isClosed;

    private List<User> connectedUsers;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    public Channel(final String channelName) {
        this.id = random(10, 0, 0, true, true, null, new SecureRandom());
        this.name = channelName;
        this.isClosed = false;
        this.connectedUsers = new ArrayList<>();
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

    public User getUser(final String userId) {
        return this.connectedUsers.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No user of id: %s in channel: %s", userId, this.id)));
    }

    public void close() {
        this.isClosed = true;
    }
}
