package io.github.gatoke.christmasdraw.port.adapter.persistence;

import io.github.gatoke.christmasdraw.domain.Channel;
import io.github.gatoke.christmasdraw.domain.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class ChannelMongoRepository implements ChannelRepository {

    private final ChannelSpringDataRepository repository;

    @Override
    public Channel save(final Channel channel) {
        return repository.save(channel);
    }

    @Override
    public Channel findOrThrow(final String channelId) {
        return repository.findById(channelId)
                .orElseThrow();//todo
    }

    @Override
    public boolean existsById(final String channelId) {
        return repository.existsById(channelId);
    }
}
