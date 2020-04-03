package com.zorg.zombies.command;

import com.zorg.zombies.model.geometry.Direction;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserMoveCommand extends MoveDirectionCommand {

    {
        moveStartCommand = true;
    }

    public UserMoveCommand(Direction moveDirection) {
        super(moveDirection);
    }
}
