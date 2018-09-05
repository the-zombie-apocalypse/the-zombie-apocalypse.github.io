package com.zorg.zombies.command;

import com.zorg.zombies.model.MoveDirection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserMoveCommand extends MoveDirectionCommand {

    {
        moveStartCommand = true;
    }

    public UserMoveCommand(String userId, MoveDirection moveDirection) {
        super(userId, moveDirection);
    }
}
