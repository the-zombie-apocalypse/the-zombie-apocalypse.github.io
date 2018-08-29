package com.zorg.zombies.change;

import com.zorg.zombies.model.User;

public class NoUserChange extends UserChange {

    {
        updated = false;
    }

    public NoUserChange(String id) {
        super(id);
    }

    public NoUserChange(User user) {
        super(user);
    }
}
