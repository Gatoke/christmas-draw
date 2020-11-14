package io.github.gatoke.christmasdraw.domain;

import lombok.Getter;

@Getter
public class User {

    private String id;
    private String name;
    private Boolean isReady;

    public User(final String id, final String name) {
        this.id = id;
        this.name = name;
        this.isReady = false;
    }

    public void switchReadyStatus() {
        this.isReady = !this.isReady;
    }
}
