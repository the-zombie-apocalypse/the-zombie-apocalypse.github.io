package com.zorg.zombies.change;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorldChange<T extends UserChange> {

    private T user;

    public WorldChange(T user) {
        this.user = user;
    }
}
