package com.zorg.zombies.service.exception;

import com.zorg.zombies.command.Command;

public class WrongMoveCommandException extends RuntimeException {
    public WrongMoveCommandException(Command command) {
        super("Wrong move command: " + command);
    }
}
