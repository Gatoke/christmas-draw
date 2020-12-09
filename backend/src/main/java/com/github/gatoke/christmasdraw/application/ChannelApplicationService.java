package com.github.gatoke.christmasdraw.application;

import com.github.gatoke.christmasdraw.domain.Channel;
import com.github.gatoke.christmasdraw.domain.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelApplicationService {

    private final ChannelRepository channelRepository;

    public Channel createChannel(final String channelName) {
        final Channel channel = new Channel(channelName);
        return channelRepository.save(channel);
    }

    public Channel addUserToChannel(final String userId, final String username, final String channelId) {
        final Channel channel = channelRepository.findOrThrow(channelId);
        channel.addUser(userId, username);
        return channelRepository.save(channel);
    }

    public Channel removeUserFromChannel(final String userId, final String channelId) {
        final Channel channel = channelRepository.findOrThrow(channelId);
        channel.removeUser(userId);
        return channelRepository.save(channel);
    }

    public Channel switchUserReadyStatus(final String userId, final String channelId) {
        final Channel channel = channelRepository.findOrThrow(channelId);
        channel.switchUserReadyStatus(userId);
        return channelRepository.save(channel);
    }

    public void closeChannel(final String channelId) {
        final Channel channel = channelRepository.findOrThrow(channelId);
        channel.close();
        channelRepository.save(channel);
    }
}
