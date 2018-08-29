package com.zorg.zombies.command;

import com.zorg.zombies.model.MoveDirection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserStopMoveCommand extends Command {

    private final MoveDirection stopMoveDirection;

    {
        stopMoveCommand = true;
    }

    public UserStopMoveCommand(String userId, MoveDirection stopMoveDirection) {
        super(userId);
        this.stopMoveDirection = stopMoveDirection;
    }
}