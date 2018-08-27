package com.zorg.zombies;

import lombok.Data;

@Data
public class WorldChange {
    private User user;

    public WorldChange(User user) {
        this.user = user;
    }
}
