package com.zorg.zombies.command.exception;

public class UserIdIsRequired extends RuntimeException {
    public UserIdIsRequired(String userId) {
        super("Wrong user id exception: " + userId);
    }
}
