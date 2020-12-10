package com.github.gatoke.christmasdraw.port.adapter.persistence;

import com.github.gatoke.christmasdraw.domain.Verify;
import com.github.gatoke.christmasdraw.domain.VerifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class VerifyMongoRepository implements VerifyRepository {

    private final VerifySpringDataRepository repository;

    @Override
    public Verify save(final Verify verify) {
        return repository.save(verify);
    }

    @Override
    public Verify findOrThrow(final String channelId) {
        return repository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Verify for channel: " + channelId + " not exists."));
    }
}

interface VerifySpringDataRepository extends MongoRepository<Verify, String> {

}
