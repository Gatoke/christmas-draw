package com.github.gatoke.christmasdraw.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class DrawResult {

    private final String performerId;
    private final String resultId;
    private final String resultDisplayName;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DrawResult that = (DrawResult) o;
        return Objects.equals(resultId, that.resultId) &&
                Objects.equals(resultDisplayName, that.resultDisplayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, resultDisplayName);
    }
}
