package com.github.gatoke.christmasdraw.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DrawAlgorithm {

    /**
     * Christmas Draw Algorithm.
     * Basically: N person draws N+1 person. If there is no person on N+1 then he draws first person from the list.
     * <p>
     * A -> B -> C -> A
     * <p>
     * So everyone will get a present.
     */
    public Set<DrawResult> perform(final List<User> users) {
        final Set<DrawResult> drawResults = new HashSet<>();

        for (int i = 0; i < users.size(); i++) {
            final User performer = users.get(i);
            if (isLastItemOnTheList(users, i)) {
                final User result = users.get(0);
                drawResults.add(
                        new DrawResult(performer.getId(), result.getId(), result.getName())
                );
            } else {
                final User result = users.get(i + 1);
                drawResults.add(
                        new DrawResult(performer.getId(), result.getId(), result.getName())
                );
            }
        }
        return drawResults;
    }

    private boolean isLastItemOnTheList(final List<User> users, final int i) {
        return i == users.size() - 1;
    }
}
