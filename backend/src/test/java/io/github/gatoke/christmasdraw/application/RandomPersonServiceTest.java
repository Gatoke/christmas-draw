package io.github.gatoke.christmasdraw.application;

import io.github.gatoke.christmasdraw.domain.Channel;
import io.github.gatoke.christmasdraw.domain.ChannelRepository;
import io.github.gatoke.christmasdraw.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RandomPersonServiceTest {

    private static final String CHANNEL_ID = "997";
    private static final String USER_ID = "1234";

    @Autowired
    private RandomPersonService service;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    void shouldDrawAllPeople() throws InterruptedException {
        // given
        final User testedUser = new User(USER_ID, "Syn", CHANNEL_ID);

        final var channel = createChannelWithInitial(testedUser);
        channelRepository.save(channel);

        // when
        final int numberOfThreads = channel.getConnectedUsers().size();
        final var threadPool = Executors.newFixedThreadPool(numberOfThreads);
        final var latch = new CountDownLatch(numberOfThreads);

        final Set<String> chosenUsers = new HashSet<>();
        for (final User user : channel.getConnectedUsers()) {
            threadPool.execute(() -> {
                final User chosenUser = service.pickRandomPersonForUserInChannel(user.getId(), channel.getId());
                chosenUsers.add(chosenUser.getId());
                latch.countDown();
            });
        }

        // then
        latch.await();
        channel.getConnectedUsers().forEach(user ->
                assertThat(user.getChosenUserId()).isNotEqualTo(user.getId())
        );
        assertThat(chosenUsers).hasSameSizeAs(channel.getConnectedUsers());
    }


    private Channel createChannelWithInitial(final User user) {
        final Channel channel = new Channel("Swiateczne losowanie");
        channel.getConnectedUsers().add(user);
        channel.addUser("756784532", "Mama");
        channel.addUser("42136412", "Tata");
        channel.addUser("ashui421", "Dziadek");
        channel.addUser("997cx9zf", "Babcia");
        channel.addUser("241243", "Basia");
        channel.addUser("5553222", "Córka");
        channel.addUser("xxff3433", "Jerzy");
        channel.addUser("aaaaaaaa", "Maciek");
        channel.addUser("67745", "Maria");
        return channel;
    }
}
