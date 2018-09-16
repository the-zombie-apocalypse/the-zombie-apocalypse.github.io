package com.zorg.zombies.change;

import com.zorg.zombies.model.MoveDirection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserStopMovingChange extends UserChange {

    private final MoveDirection stopMoveDirection;

    {
        stopMovingChange = true;
    }

    public UserStopMovingChange(String id, MoveDirection stopMoveDirection) {
        super(id);
        this.stopMoveDirection = stopMoveDirection;
    }

}

