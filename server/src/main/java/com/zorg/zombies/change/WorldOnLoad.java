package com.zorg.zombies.change;

import com.zorg.zombies.model.Coordinates;

public class WorldOnLoad extends WorldChange {
    public WorldOnLoad(UserChange user) {
        super(user);
    }

    public WorldOnLoad(String id, Coordinates coordinates) {
        super(new UserChange(id, coordinates));
    }
}
