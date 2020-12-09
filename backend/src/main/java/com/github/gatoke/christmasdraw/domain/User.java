package com.github.gatoke.christmasdraw.domain;

import lombok.Getter;

@Getter
public class User {

    private String id;
    private String name;
    private String channelId;
    private Boolean isReady;
    private Boolean hasSentVerifyMessage;

    public User(final String id, final String name, final String channelId) {
        this.id = id;
        this.name = name;
        this.channelId = channelId;
        this.isReady = false;
        this.hasSentVerifyMessage = false;
    }

    public void switchReadyStatus() {
        this.isReady = !this.isReady;
    }

    public void setSentVerifyMessage() {
        this.hasSentVerifyMessage = true;
    }

    public boolean hasSentVerifyMessage() {
        return this.hasSentVerifyMessage;
    }
}
