package com.zorg.zombies.change;

import lombok.Data;

@Data
public class WorldChange<T extends UserChange> {

    private final T user;

    public WorldChange(T user) {
        this.user = user;
    }

    public WorldChange() {
        this(null);
    }
}
