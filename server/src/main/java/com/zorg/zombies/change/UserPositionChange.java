package com.zorg.zombies.change;

import com.zorg.zombies.model.Coordinates;
import com.zorg.zombies.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserPositionChange extends UserChange {
    {
        positionChange = true;
    }

    private Coordinates coordinates;

    @java.beans.ConstructorProperties({"id", "coordinates"})
    public UserPositionChange(String id, Coordinates coordinates) {
        super(id);
        this.coordinates = coordinates;
    }

    public UserPositionChange(User user) {
        this(user.getId(), user.getCoordinates());
    }
}
