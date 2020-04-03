package com.zorg.zombies.command;

import com.zorg.zombies.model.geometry.Direction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MoveDirectionCommand extends Command {

    public static final String DIRECTION_FIELD = "direction";

    protected final Direction direction;

    MoveDirectionCommand(Direction direction) {
        this.direction = direction;
    }
}
