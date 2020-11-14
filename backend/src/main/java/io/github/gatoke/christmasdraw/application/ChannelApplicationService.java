package io.github.gatoke.christmasdraw.application;

import io.github.gatoke.christmasdraw.domain.Channel;
import io.github.gatoke.christmasdraw.domain.ChannelRepository;
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

    public Channel addUserToChannel(final String username, final String channelId) {
        final Channel channel = channelRepository.findOrThrow(channelId);
        channel.addUser(username);
        return channelRepository.save(channel);
    }

    public Channel removeUserFromChannel(final String username, final String channelId) {
        final Channel channel = channelRepository.findOrThrow(channelId);
        channel.removeUser(username);
        return channelRepository.save(channel);
    }
}
