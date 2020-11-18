package io.github.gatoke.christmasdraw.application;

import io.github.gatoke.christmasdraw.domain.Channel;
import io.github.gatoke.christmasdraw.domain.ChannelRepository;
import io.github.gatoke.christmasdraw.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RandomPersonService {

    private final ChannelRepository channelRepository;
    private final Random random = new Random();

    // todo distributed lock
    public synchronized User pickRandomPersonForUserInChannel(final String userId, final String channelId) {
        final Channel channel = channelRepository.findOrThrow(channelId);
        final User picker = channel.getUser(userId);

        final Set<User> notPickedUsers = channel.getNotPickedUsersExcept(userId);
        final List<User> preparedForChoosing = prepareForRandomTake(notPickedUsers, picker.getId());

        final User chosenPerson;
        if (preparedForChoosing.size() == 2) {
            chosenPerson = preparedForChoosing.get(0).getChosenUserId() == null ? preparedForChoosing.get(0) : preparedForChoosing.get(1);
        } else {
            chosenPerson = preparedForChoosing.get(random.nextInt(preparedForChoosing.size()));
        }

        picker.chooseUserWithId(chosenPerson.getId());
        chosenPerson.setIsChosen();
        channelRepository.save(channel);

        return chosenPerson;
    }

    private List<User> prepareForRandomTake(final Set<User> users, final String pickerId) {
        if (users.size() == 2) { // prevent user stay without being chosen :(
            return users.stream()
                    .filter(user -> user.getChosenUserId() == null || !user.getChosenUserId().equals(pickerId))
                    .collect(toList());
        }
        return new ArrayList<>(users);
    }
}
