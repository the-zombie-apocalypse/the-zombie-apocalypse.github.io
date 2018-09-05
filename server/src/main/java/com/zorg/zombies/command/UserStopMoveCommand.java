package com.zorg.zombies.command;

import com.zorg.zombies.model.MoveDirection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserStopMoveCommand extends MoveDirectionCommand {

    {
        moveStopCommand = true;
    }

    public UserStopMoveCommand(String userId, MoveDirection moveStopDirection) {
        super(userId, moveStopDirection);
    }
}
