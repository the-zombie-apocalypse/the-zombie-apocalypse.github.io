package com.zorg.zombies.change;

import com.zorg.zombies.model.Coordinates;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorldOnLoad extends WorldChange<UserPositionChange> {

    private boolean isGreeting = true;

    public WorldOnLoad(UserPositionChange user) {
        super(user);
    }

    public WorldOnLoad(String id, Coordinates coordinates) {
        super(new UserPositionChange(id, coordinates));
    }
}
