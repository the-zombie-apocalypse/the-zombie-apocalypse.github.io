package com.zorg.zombies.change;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserLeftGameEvent extends UserChange {

    private final boolean leavingGameEvent = true;

    public UserLeftGameEvent(String id) {
        super(id);
    }
}
