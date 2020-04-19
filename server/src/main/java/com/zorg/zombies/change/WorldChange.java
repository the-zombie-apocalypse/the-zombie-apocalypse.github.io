package com.zorg.zombies.change;

import lombok.Data;

@Data
public class WorldChange<T extends UserChange> {

    private final T user;
    private final boolean greeting;

    public WorldChange(T user) {
        this.user = user;
        greeting = false;
    }

    public WorldChange(T user, boolean isGreeting) {
        this.user = user;
        greeting = isGreeting;
    }

    public WorldChange() {
        this(null);
    }
}
