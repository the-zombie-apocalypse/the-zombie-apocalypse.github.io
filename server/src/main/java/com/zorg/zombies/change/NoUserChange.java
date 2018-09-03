package com.zorg.zombies.change;

public class NoUserChange extends UserChange {

    {
        updated = false;
    }

    public NoUserChange(String id) {
        super(id);
    }
}
