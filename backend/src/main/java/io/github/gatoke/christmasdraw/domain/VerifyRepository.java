package io.github.gatoke.christmasdraw.domain;

public interface VerifyRepository {

    Verify save(final Verify verify);

    Verify findOrThrow(final String channelId);
}
