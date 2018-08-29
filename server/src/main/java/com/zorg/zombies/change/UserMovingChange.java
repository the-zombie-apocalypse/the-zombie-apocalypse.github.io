package com.zorg.zombies.change;

import com.zorg.zombies.model.MoveDirection;
import com.zorg.zombies.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserMovingChange extends UserChange {

    private final MoveDirection moveDirection;

    public UserMovingChange(String id, MoveDirection moveDirection) {
        super(id);
        this.moveDirection = moveDirection;
    }

    public UserMovingChange(User user, MoveDirection moveDirection) {
        super(user);
        this.moveDirection = moveDirection;
    }
}
