package com.zorg.zombies.model;

import lombok.Data;

import static com.zorg.zombies.model.MoveDirectionX.*;
import static com.zorg.zombies.model.MoveDirectionY.*;
import static com.zorg.zombies.model.MoveDirectionZ.*;

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

    public boolean isMoving(MoveDirection direction) {
        if (NORTH.equals(direction)) {
            return isMovingNorth;

        } else if (EAST.equals(direction)) {
            return isMovingEast;

        } else if (SOUTH.equals(direction)) {
            return isMovingSouth;

        } else if (WEST.equals(direction)) {
            return isMovingWest;

        } else if (UP.equals(direction)) {
            return isMovingUp;

        } else if (DOWN.equals(direction)) {
            return isMovingDown;

        } else return false;
    }

    /**
     * Setting move without any checking!
     */
    public void setMoving(MoveDirection direction) {
        if (NORTH.equals(direction)) {
            setMovingNorth(true);

        } else if (EAST.equals(direction)) {
            setMovingEast(true);

        } else if (SOUTH.equals(direction)) {
            setMovingSouth(true);

        } else if (WEST.equals(direction)) {
            setMovingWest(true);

        } else if (UP.equals(direction)) {
            setMovingUp(true);

        } else if (DOWN.equals(direction)) {
            setMovingDown(true);
        }
    }

    /**
     * Setting move without any checking!
     */
    public void setStopMoving(MoveDirection stopMoving) {
        if (NORTH.equals(stopMoving)) {
            setMovingNorth(false);

        } else if (EAST.equals(stopMoving)) {
            setMovingEast(false);

        } else if (SOUTH.equals(stopMoving)) {
            setMovingSouth(false);

        } else if (WEST.equals(stopMoving)) {
            setMovingWest(false);

        } else if (UP.equals(stopMoving)) {
            setMovingUp(false);

        } else if (DOWN.equals(stopMoving)) {
            setMovingDown(false);
        }
    }

}
