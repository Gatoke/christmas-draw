package io.github.gatoke.christmasdraw.domain;

import lombok.Getter;

@Getter
public class User {

    private String id;
    private String name;
    private String channelId;
    private Boolean isReady;
    private String chosenUserId;
    private Boolean isChosen;

    public User(final String id, final String name, final String channelId) {
        this.id = id;
        this.name = name;
        this.channelId = channelId;
        this.isReady = false;
        this.chosenUserId = null;
        this.isChosen = false;
    }

    public void switchReadyStatus() {
        this.isReady = !this.isReady;
    }

    public void chooseUserWithId(final String userId) {
        if (this.chosenUserId != null) {
            throw new IllegalStateException("User can't be chosen 2 times!");
        }
        this.chosenUserId = userId;
    }

    public void setIsChosen() {
        this.isChosen = true;
    }
}
