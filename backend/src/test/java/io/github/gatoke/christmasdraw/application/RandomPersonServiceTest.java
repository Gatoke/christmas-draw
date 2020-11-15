package io.github.gatoke.christmasdraw.application;

import io.github.gatoke.christmasdraw.domain.Channel;
import io.github.gatoke.christmasdraw.domain.ChannelRepository;
import io.github.gatoke.christmasdraw.domain.User;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RandomPersonServiceTest {


    private static final String CHANNEL_ID = "997";
    private static final String USER_ID = "1234";

    @Test
    void shouldDrawRandomPerson() {
        // given
        final User testedUser = new User(USER_ID, "Syn", CHANNEL_ID);

        final var repository = mock(ChannelRepository.class);
        when(repository.findOrThrow(any())).thenReturn(createChannelWithInitial(testedUser));

        final var service = new RandomPersonService(repository);

        // when
        final User user = service.pickRandomPersonForUserInChannel(USER_ID, CHANNEL_ID);

        // then
        assertThat(testedUser.getChosenUserId()).isNotNull();
        assertThat(testedUser.getChosenUserId()).isEqualTo(user.getId());
    }

    @Test
    void testShouldDrawAllPeople() { // neuralgic functionality - prevent random failing
        for (int i = 0; i < 50; i++) {
            shouldDrawAllPeople();
        }
    }

    private void shouldDrawAllPeople() {
        // given
        final User testedUser = new User(USER_ID, "Syn", CHANNEL_ID);

        final var repository = mock(ChannelRepository.class);
        final var channel = createChannelWithInitial(testedUser);
        when(repository.findOrThrow(any())).thenReturn(channel);

        final var service = new RandomPersonService(repository);

        // when
        final Set<String> chosenUsers = new HashSet<>();
        channel.getConnectedUsers().forEach(user -> {
            final User chosenUser = service.pickRandomPersonForUserInChannel(user.getId(), channel.getId());
            chosenUsers.add(chosenUser.getId());
        });

        // then
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
        return channel;
    }
}
