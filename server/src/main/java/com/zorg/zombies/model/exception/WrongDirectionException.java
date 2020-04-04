package com.zorg.zombies.model.exception;

import com.zorg.zombies.model.geometry.Direction;

public class WrongDirectionException extends RuntimeException {
    public WrongDirectionException(Direction direction) {
        super("Wrong direction " + direction);
    }
}
