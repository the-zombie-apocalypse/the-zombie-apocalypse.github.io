package com.zorg.zombies.command;

import lombok.Data;

@Data
public class Command {

    public static final String MOVE_COMMAND_FIELD = "moveCommand";
    public static final String MOVE_DIRECTION_FIELD = "moveDirection";
    protected boolean moveCommand;
    protected boolean stopMoveCommand;
    private String userId;

    public Command(String userId) {
        this.userId = userId;
    }
}
