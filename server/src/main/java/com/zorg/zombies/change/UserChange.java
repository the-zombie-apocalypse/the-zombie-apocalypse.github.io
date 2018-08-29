package com.zorg.zombies.change;

import com.zorg.zombies.model.Coordinates;
import com.zorg.zombies.model.MoveDirectionX;
import com.zorg.zombies.model.MoveDirectionY;
import com.zorg.zombies.model.MoveDirectionZ;
import com.zorg.zombies.model.User;
import lombok.Data;

@Data
public class UserChange {

    protected boolean updated = true;

    private String id;

    private Coordinates coordinates;

    private MoveDirectionX stopMovingX;
    private MoveDirectionY stopMovingY;
    private MoveDirectionZ stopMovingZ;

    public UserChange(String id) {
        this.id = id;
    }

    public UserChange(User user) {
        this.id = user.getId();
    }
}
