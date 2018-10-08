package com.zorg.zombies.change;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewUserJoined extends WorldChange<UserPositionChange> {

    private boolean isGreeting = true;

    public NewUserJoined(UserPositionChange user) {
        super(user);
    }

}
