package com.zorg.zombies.change;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorldChange {

    private UserChange user;

    public WorldChange(UserChange user) {
        this.user = user;
    }
}
