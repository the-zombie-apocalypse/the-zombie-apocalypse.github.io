package com.zorg.zombies.change;

import com.zorg.zombies.change.UserChange;
import lombok.Data;

@Data
public class WorldChange {

    private UserChange user;

    public WorldChange(UserChange user) {
        this.user = user;
    }
}
