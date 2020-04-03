package com.zorg.zombies.change;

import com.zorg.zombies.model.geometry.Direction;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserStopMovingChange extends UserChange {

    protected boolean stopMovingChange = true;
    private final Direction stopMoveDirection;

    public UserStopMovingChange(String id, Direction stopMoveDirection) {
        super(id);
        this.stopMoveDirection = stopMoveDirection;
    }

}

