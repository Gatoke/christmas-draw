package com.github.gatoke.christmasdraw.application;

import com.github.gatoke.christmasdraw.domain.Channel;
import com.github.gatoke.christmasdraw.domain.ChannelRepository;
import com.github.gatoke.christmasdraw.domain.DrawResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DrawApplicationServiceTest {

    @Autowired
    private DrawApplicationService service;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    void shouldDrawAllPeople() {
        // given
        final var channel = createChannel();
        channelRepository.save(channel);

        // when
        final Set<DrawResult> result = service.performDraw(channel.getId());

        // then
        assertThat(result).hasSameSizeAs(channel.getConnectedUsers());
        result.forEach(drawResult -> assertThat(drawResult.getPerformerId()).isNotEqualTo(drawResult.getResultId()));
    }

    private Channel createChannel() {
        final Channel channel = new Channel("Swiateczne losowanie");
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
