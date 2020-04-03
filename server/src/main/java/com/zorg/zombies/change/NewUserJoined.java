package com.zorg.zombies.change;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NewUserJoined extends WorldChange<UserPositionChange> {

    private final boolean isGreeting = true;

    public NewUserJoined(UserPositionChange user) {
        super(user);
    }

}
