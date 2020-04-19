package com.zorg.zombies.change;

import com.zorg.zombies.model.Coordinates;
import com.zorg.zombies.model.UserData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserPositionChange extends UserChange {

    private final boolean positionChange = true;
    private final Coordinates coordinates;

    @java.beans.ConstructorProperties({"id", "coordinates", "nickname"})
    public UserPositionChange(String id, Coordinates coordinates, String nickname) {
        super(id, nickname);
        this.coordinates = coordinates;
    }

    /**
     * @deprecated use {@link UserPositionChange#UserPositionChange(java.lang.String, com.zorg.zombies.model.Coordinates, java.lang.String)}}
     */
    @Deprecated
    public UserPositionChange(String id, Coordinates coordinates) {
        super(id, null);
        this.coordinates = coordinates;
    }

    public UserPositionChange(UserData user) {
        this(user.getId(), user.getCoordinates(), user.getNickname());
    }
}
