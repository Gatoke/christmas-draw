package io.github.gatoke.christmasdraw.domain.event;

public enum EventType {

    USER_CONNECTED,
    USER_DISCONNECTED,
    USER_READY_STATUS_CHANGED,
    ALL_USERS_READY,
    RANDOM_PERSON_PICKED,
    VERIFICATION_MESSAGE_RECEIVED,
    RESULTS_VERIFIED;
}
