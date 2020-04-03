package com.zorg.zombies.change;

import com.zorg.zombies.model.geometry.Direction;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserMovingChange extends UserChange {

    private final boolean movingChange = true;
    private final Direction moveDirection;

    public UserMovingChange(String id, Direction moveDirection) {
        super(id);
        this.moveDirection = moveDirection;
    }

}
