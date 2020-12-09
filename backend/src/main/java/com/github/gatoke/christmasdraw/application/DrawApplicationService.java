package com.github.gatoke.christmasdraw.application;

import com.github.gatoke.christmasdraw.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Collections.shuffle;

@Service
@RequiredArgsConstructor
public class DrawApplicationService {

    private final ChannelRepository channelRepository;

    private final Random random = ThreadLocalRandom.current();
    private final DrawAlgorithm algorithm = new DrawAlgorithm();

    public Set<DrawResult> performDraw(final String channelId) {
        final Channel channel = channelRepository.findOrThrow(channelId);
        if (channel.getIsClosed()) {
            throw new IllegalStateException("Channel is already closed!");
        }
        final List<User> users = channel.getConnectedUsers();
        shuffle(users, random);
        return algorithm.perform(users);
    }
}
