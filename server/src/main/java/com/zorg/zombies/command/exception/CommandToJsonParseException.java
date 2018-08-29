package com.zorg.zombies.command.exception;

public class CommandToJsonParseException extends RuntimeException {
    public CommandToJsonParseException(String jsonCommand) {
        super("Can not parse command from json: " + jsonCommand);
    }
}
