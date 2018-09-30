package com.zorg.zombies.util;

import com.zorg.zombies.model.MoveDirection;
import com.zorg.zombies.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.zorg.zombies.model.MoveDirectionX.*;
import static com.zorg.zombies.model.MoveDirectionY.*;
import static com.zorg.zombies.model.MoveDirectionZ.*;

public class MovementAndDirections {

    public static boolean isMoving(User user) {
        return (user.isMovingNorth()
                || user.isMovingSouth()
                || user.isMovingWest()
                || user.isMovingEast()
                || user.isMovingUp()
                || user.isMovingDown()
        );
    }

    public static void setMoving(MoveDirection direction, User user) {
        if (NORTH.equals(direction)) user.setMovingNorth(true);
        else if (EAST.equals(direction)) user.setMovingEast(true);
        else if (SOUTH.equals(direction)) user.setMovingSouth(true);
        else if (WEST.equals(direction)) user.setMovingWest(true);
        else if (UP.equals(direction)) user.setMovingUp(true);
        else if (DOWN.equals(direction)) user.setMovingDown(true);
    }

    public static boolean isMoving(MoveDirection direction, User user) {
        if (NORTH.equals(direction)) return user.isMovingNorth();
        else if (EAST.equals(direction)) return user.isMovingEast();
        else if (SOUTH.equals(direction)) return user.isMovingSouth();
        else if (WEST.equals(direction)) return user.isMovingWest();
        else if (UP.equals(direction)) return user.isMovingUp();
        else if (DOWN.equals(direction)) return user.isMovingDown();
        else return false;
    }

    public static void setStopMoving(MoveDirection stopMoving, User user) {
        if (NORTH.equals(stopMoving)) user.setMovingNorth(false);
        else if (EAST.equals(stopMoving)) user.setMovingEast(false);
        else if (SOUTH.equals(stopMoving)) user.setMovingSouth(false);
        else if (WEST.equals(stopMoving)) user.setMovingWest(false);
        else if (UP.equals(stopMoving)) user.setMovingUp(false);
        else if (DOWN.equals(stopMoving)) user.setMovingDown(false);
    }

    public static List<MoveDirection> collectMovingDirections(User user) {
        final List<MoveDirection> directions = new ArrayList<>();

        if (user.isMovingNorth()) directions.add(NORTH);
        else if (user.isMovingSouth()) directions.add(SOUTH);
        if (user.isMovingWest()) directions.add(WEST);
        else if (user.isMovingEast()) directions.add(EAST);
        if (user.isMovingUp()) directions.add(UP);
        else if (user.isMovingDown()) directions.add(DOWN);

        return Collections.unmodifiableList(directions);
    }
}
