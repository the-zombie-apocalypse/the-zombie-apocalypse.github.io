package com.zorg.zombies.util;

import com.zorg.zombies.model.User;
import com.zorg.zombies.model.geometry.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.zorg.zombies.model.geometry.Direction.DOWN;
import static com.zorg.zombies.model.geometry.Direction.EAST;
import static com.zorg.zombies.model.geometry.Direction.NORTH;
import static com.zorg.zombies.model.geometry.Direction.SOUTH;
import static com.zorg.zombies.model.geometry.Direction.UP;
import static com.zorg.zombies.model.geometry.Direction.WEST;

public class MovementAndDirections {

    public static boolean isMoving(User user) {
        return user.isMovingNorth()
                || user.isMovingSouth()
                || user.isMovingWest()
                || user.isMovingEast()
                || user.isMovingUp()
                || user.isMovingDown();
    }

    public static void setMoving(Direction direction, User user) {
        getMovingToggle(direction, user).accept(true);
    }

    public static boolean isMoving(Direction direction, User user) {
        switch (direction) {
            case NORTH:
                return user.isMovingNorth();
            case EAST:
                return user.isMovingEast();
            case SOUTH:
                return user.isMovingSouth();
            case WEST:
                return user.isMovingWest();
            case UP:
                return user.isMovingUp();
            case DOWN:
                return user.isMovingDown();
            default:
                throw new WrongDirectionException(direction);
        }
    }

    public static void setStopMoving(Direction stopMoving, User user) {
        getMovingToggle(stopMoving, user).accept(false);
    }

    private static Consumer<Boolean> getMovingToggle(Direction stopMoving, User user) {
        switch (stopMoving) {
            case NORTH:
                return user::setMovingNorth;
            case EAST:
                return user::setMovingEast;
            case SOUTH:
                return user::setMovingSouth;
            case WEST:
                return user::setMovingWest;
            case UP:
                return user::setMovingUp;
            case DOWN:
                return user::setMovingDown;
            default:
                throw new WrongDirectionException(stopMoving);
        }
    }

    public static List<Direction> collectMovingDirections(User user) {
        final List<Direction> directions = new ArrayList<>();

        if (user.isMovingNorth()) directions.add(NORTH);
        else if (user.isMovingSouth()) directions.add(SOUTH);
        if (user.isMovingWest()) directions.add(WEST);
        else if (user.isMovingEast()) directions.add(EAST);
        if (user.isMovingUp()) directions.add(UP);
        else if (user.isMovingDown()) directions.add(DOWN);

        return Collections.unmodifiableList(directions);
    }

    public static class WrongDirectionException extends RuntimeException {
        WrongDirectionException(Direction direction) {
            super("Wrong direction " + direction);
        }
    }
}
