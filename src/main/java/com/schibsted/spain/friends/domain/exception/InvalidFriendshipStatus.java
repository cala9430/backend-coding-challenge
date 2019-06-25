package com.schibsted.spain.friends.domain.exception;

public class InvalidFriendshipStatus extends RuntimeException {
    public InvalidFriendshipStatus(String message) {
        super(message);
    }
}
