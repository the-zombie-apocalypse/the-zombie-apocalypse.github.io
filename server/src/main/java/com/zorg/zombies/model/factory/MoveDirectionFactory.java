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
            case "north": return NORTH;
            case "east": return EAST;
            case "south": return SOUTH;
            case "west": return WEST;
            case "up": return UP;
            case "down": return DOWN;
        }

        throw new WrongMoveDirectionException(direction);
    }
}
