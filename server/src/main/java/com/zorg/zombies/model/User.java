package com.zorg.zombies.model;

import com.zorg.zombies.service.GameActionsProcessor;
import lombok.Getter;
import lombok.Setter;

import static com.zorg.zombies.model.MoveDirectionX.*;
import static com.zorg.zombies.model.MoveDirectionY.*;
import static com.zorg.zombies.model.MoveDirectionZ.*;

@Getter
@Setter
public class User extends UserData {

    private int visibleDistance = 50; // dummy value for now

    private volatile boolean isMovingNorth;
    private volatile boolean isMovingSouth;
    private volatile boolean isMovingWest;
    private volatile boolean isMovingEast;
    private volatile boolean isMovingUp;
    private volatile boolean isMovingDown;

    private GameActionsProcessor processor;

    public User(String id) {
        super(id, new Coordinates(0, 0)); // todo: receive actual coordinates from somewhere...
    }

    public boolean isMoving() {
        return (isMovingNorth || isMovingSouth || isMovingWest || isMovingEast || isMovingUp || isMovingDown);
    }

    public boolean isMoving(MoveDirection direction) {
        if (NORTH.equals(direction)) return isMovingNorth;
        else if (EAST.equals(direction)) return isMovingEast;
        else if (SOUTH.equals(direction)) return isMovingSouth;
        else if (WEST.equals(direction)) return isMovingWest;
        else if (UP.equals(direction)) return isMovingUp;
        else if (DOWN.equals(direction)) return isMovingDown;
        else return false;
    }

    /**
     * Setting move without any checking!
     */
    public void setMoving(MoveDirection direction) {
        if (NORTH.equals(direction)) setMovingNorth(true);
        else if (EAST.equals(direction)) setMovingEast(true);
        else if (SOUTH.equals(direction)) setMovingSouth(true);
        else if (WEST.equals(direction)) setMovingWest(true);
        else if (UP.equals(direction)) setMovingUp(true);
        else if (DOWN.equals(direction)) setMovingDown(true);
    }

    /**
     * Setting move without any checking!
     */
    public void setStopMoving(MoveDirection stopMoving) {
        if (NORTH.equals(stopMoving)) setMovingNorth(false);
        else if (EAST.equals(stopMoving)) setMovingEast(false);
        else if (SOUTH.equals(stopMoving)) setMovingSouth(false);
        else if (WEST.equals(stopMoving)) setMovingWest(false);
        else if (UP.equals(stopMoving)) setMovingUp(false);
        else if (DOWN.equals(stopMoving)) setMovingDown(false);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
