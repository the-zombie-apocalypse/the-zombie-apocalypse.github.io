package com.zorg.zombies.model.factory;

import com.zorg.zombies.model.MoveDirection;
import org.springframework.stereotype.Component;

import static com.zorg.zombies.model.MoveDirectionX.*;
import static com.zorg.zombies.model.MoveDirectionY.*;
import static com.zorg.zombies.model.MoveDirectionZ.*;

@Component
public class MoveDirectionFactory {

    public MoveDirection parseMoveDirection(String direction) {
        switch (direction) {
            case "NORTH":
                return NORTH;
            case "EAST":
                return EAST;
            case "SOUTH":
                return SOUTH;
            case "WEST":
                return WEST;
            case "UP":
                return UP;
            case "DOWN":
                return DOWN;
        }

        throw new WrongMoveDirectionException(direction);
    }
}
