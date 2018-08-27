package com.zorg.zombies;

import lombok.Data;

@Data
public class User {

    private String id;
    private Coordinates coordinates = new Coordinates();
    private int visibleDistance = 50; // dummy value for now

    private boolean isMovingUp;
    private boolean isStopMovingUp;

    private boolean isMovingDown;
    private boolean isStopMovingDown;

    private boolean isMovingLeft;
    private boolean isStopMovingLeft;

    private boolean isMovingRight;
    private boolean isStopMovingRight;

    public User(String id) {
        this.id = id;
    }
}
