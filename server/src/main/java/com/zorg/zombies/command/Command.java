package com.zorg.zombies.command;

import lombok.Data;

@Data
public class Command {

    public static final String MOVE_COMMAND_FIELD = "moveStartCommand";

    protected boolean moveStartCommand;
    protected boolean moveStopCommand;

    private String userId;

    public Command(String userId) {
        this.userId = userId;
    }

    public boolean isMoveChangeCommand() {
        return moveStartCommand || moveStopCommand;
    }
}
