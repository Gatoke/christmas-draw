package com.github.gatoke.christmasdraw.application;

import com.github.gatoke.christmasdraw.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class VerifyApplicationService {

    private final VerifyRepository verifyRepository;
    private final ChannelRepository channelRepository;

    public void createVerifyForChannel(final String channelId, final int numberOfUsers) {
        verifyRepository.save(
                new Verify(channelId, numberOfUsers)
        );
    }

    public Verify sendMessageToVerify(final String channelId, final String userId, final String message) {
        final Channel channel = channelRepository.findOrThrow(channelId);
        final User sender = channel.getUser(userId);

        if (sender.hasSentVerifyMessage()) {
            throw new IllegalArgumentException(format("User: %s has already sent a verification message.", userId));
        }

        final Verify verify = verifyRepository.findOrThrow(channelId);
        verify.addMessage(message);
        channel.getUser(userId).setSentVerifyMessage();

        channelRepository.save(channel);
        return verifyRepository.save(verify);
    }
}
