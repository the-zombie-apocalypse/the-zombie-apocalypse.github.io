package com.zorg.zombies.model.exception;

import com.zorg.zombies.model.geometry.Direction;

public class WrongDirectionException extends RuntimeException {

    public WrongDirectionException(String direction) {
        super("Wrong direction " + direction);
    }

    public WrongDirectionException(Direction direction) {
        super("Wrong direction " + direction);
    }
}
