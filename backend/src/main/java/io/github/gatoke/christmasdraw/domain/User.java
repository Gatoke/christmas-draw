package io.github.gatoke.christmasdraw.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User {

    private String id;
    private String name;
    private Boolean isReady;

    public User(final String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.isReady = false;
    }

    public void switchReadyStatus() {
        this.isReady = !this.isReady;
    }
}
