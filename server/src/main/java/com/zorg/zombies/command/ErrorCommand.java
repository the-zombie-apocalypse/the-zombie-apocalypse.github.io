package com.zorg.zombies.command;

public class ErrorCommand extends Command {

    {
        isErrorCommand = true;
    }

    public ErrorCommand(Exception e) {
        super("error " + e);
        error = e;
    }
}
