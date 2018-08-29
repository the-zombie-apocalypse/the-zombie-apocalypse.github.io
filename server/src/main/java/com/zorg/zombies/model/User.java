package com.zorg.zombies.model;

import lombok.Data;

@Data
public class User {

    private String id;
    private Coordinates coordinates;
    private int visibleDistance = 50; // dummy value for now

    private boolean isMovingNorth;
    private boolean isMovingSouth;
    private boolean isMovingWest;
    private boolean isMovingEast;
    private boolean isMovingUp;
    private boolean isMovingDown;

    public User(String id) {
        this.id = id;
    }

    public boolean isMoving() {
        return (isMovingNorth || isMovingSouth || isMovingWest || isMovingEast || isMovingUp || isMovingDown);
    }

}
