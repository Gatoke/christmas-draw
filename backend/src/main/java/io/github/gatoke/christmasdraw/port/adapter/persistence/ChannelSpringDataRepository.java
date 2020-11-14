package io.github.gatoke.christmasdraw.port.adapter.persistence;

import io.github.gatoke.christmasdraw.domain.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChannelSpringDataRepository extends MongoRepository<Channel, String> {

}
