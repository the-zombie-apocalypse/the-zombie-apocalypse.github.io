package com.zorg.zombies;

import lombok.Data;

@Data
public class User {

    private String id;
    private Coordinates coordinates = new Coordinates();
    private int visibleDistance = 50; // dummy value for now

    private boolean isMovingUp;

    public User(String id) {
        this.id = id;
    }
}
