package com.zorg.zombies.change;

import com.zorg.zombies.model.Coordinates;
import com.zorg.zombies.model.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OnLoadUserChange extends UserPositionChange {

    public OnLoadUserChange() {
        this(null, null, null);
    }

    public OnLoadUserChange(User user) {
        super(user);
    }

    public OnLoadUserChange(String id, String nickname, Coordinates coordinates) {
        super(id, coordinates, nickname);
    }
}
