package com.zorg.zombies.model.factory;

public class WrongMoveDirectionException extends RuntimeException {
    public WrongMoveDirectionException(String direction) {
        super("Wrong user move direction: " + direction);
    }
}
