package com.zorg.zombies.command;

import lombok.Data;

@Data
public class Command {

    public static final String MOVE_COMMAND_FIELD = "moveCommand";
    public static final String MOVE_DIRECTION_FIELD = "moveDirection";

    private String userId;

    protected boolean moveCommand;
    protected boolean stopMoveCommand;

    public Command(String userId) {
        this.userId = userId;
    }
}
