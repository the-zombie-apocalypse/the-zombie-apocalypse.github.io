package com.zorg.zombies.command.exception;

public class WrongUserIdException extends RuntimeException {
    public WrongUserIdException(String userId) {
        super("Wrong user id exception: " + userId);
    }
}
