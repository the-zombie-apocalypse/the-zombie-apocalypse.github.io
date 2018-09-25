package com.zorg.zombies.change;

import com.zorg.zombies.model.Coordinates;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorldOnLoad extends WorldChange {

    private boolean isGreeting = true;

    public WorldOnLoad(UserChange user) {
        super(user);
    }

    public WorldOnLoad(String id, Coordinates coordinates) {
        super(new UserChange(id, coordinates));
    }
}
