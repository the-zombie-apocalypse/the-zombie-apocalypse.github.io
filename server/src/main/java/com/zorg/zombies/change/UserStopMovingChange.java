package com.zorg.zombies.change;

import com.zorg.zombies.model.geometry.Direction;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserStopMovingChange extends UserChange {

    private final Direction stopMoveDirection;
    protected boolean stopMovingChange = true;

    public UserStopMovingChange(String id, Direction stopMoveDirection) {
        super(id);
        this.stopMoveDirection = stopMoveDirection;
    }

}

