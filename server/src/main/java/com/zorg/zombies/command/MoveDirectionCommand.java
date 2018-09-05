package com.zorg.zombies.command;

import com.zorg.zombies.model.MoveDirection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MoveDirectionCommand extends Command {

    protected final MoveDirection direction;

    MoveDirectionCommand(String userId, MoveDirection direction) {
        super(userId);
        this.direction = direction;
    }
}
