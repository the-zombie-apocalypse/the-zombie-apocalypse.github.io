package com.zorg.zombies.change;

import com.zorg.zombies.model.MoveDirection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserMovingChange extends UserChange {

    private final MoveDirection moveDirection;

    {
        movingChange = true;
    }

    public UserMovingChange(String id, MoveDirection moveDirection) {
        super(id);
        this.moveDirection = moveDirection;
    }

}
