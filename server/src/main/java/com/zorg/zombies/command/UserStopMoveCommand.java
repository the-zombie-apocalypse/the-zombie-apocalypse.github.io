package com.zorg.zombies.command;

import com.zorg.zombies.model.geometry.Direction;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserStopMoveCommand extends MoveDirectionCommand {

    {
        moveStopCommand = true;
    }

    public UserStopMoveCommand(Direction moveStopDirection) {
        super(moveStopDirection);
    }
}
