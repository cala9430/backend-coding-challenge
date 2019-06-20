package com.schibsted.spain.friends.domain.exception;

public class DuplicatedUserException extends RuntimeException {

    public DuplicatedUserException(String message) {
        super(message);
    }
}
