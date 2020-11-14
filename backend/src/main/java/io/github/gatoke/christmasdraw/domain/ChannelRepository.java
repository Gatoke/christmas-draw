package io.github.gatoke.christmasdraw.domain;

public interface ChannelRepository {

    Channel save(final Channel channel);

    Channel findOrThrow(final String channelId);

    boolean existsById(final String channelId);
}
