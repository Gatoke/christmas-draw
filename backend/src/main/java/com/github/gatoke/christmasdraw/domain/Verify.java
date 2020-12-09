package com.github.gatoke.christmasdraw.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Collections.shuffle;

@Getter
@Document(collection = "session")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Verify {

    @Id
    private String channelId;

    private List<String> messages;

    private Boolean isVerified;

    private int targetNumberOfMessages;

    public Verify(final String channelId, final int targetNumberOfMessages) {
        this.channelId = channelId;
        this.messages = new ArrayList<>();
        this.isVerified = false;
        this.targetNumberOfMessages = targetNumberOfMessages;
    }

    public void addMessage(final String message) {
        this.messages.add(message);
        if (this.messages.size() >= targetNumberOfMessages) {
            this.isVerified = true;
        }
        shuffle(this.messages, new Random());
    }

    public boolean isVerified() {
        return this.isVerified;
    }
}
