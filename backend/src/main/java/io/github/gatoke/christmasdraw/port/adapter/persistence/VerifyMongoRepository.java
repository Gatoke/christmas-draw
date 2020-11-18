package io.github.gatoke.christmasdraw.port.adapter.persistence;

import io.github.gatoke.christmasdraw.domain.Verify;
import io.github.gatoke.christmasdraw.domain.VerifyRepository;
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
                .orElseThrow();//todo
    }
}

interface VerifySpringDataRepository extends MongoRepository<Verify, String> {

}
