package com.zorg.zombies.service;

import com.zorg.zombies.change.NoUserChange;
import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserMovingChange;
import com.zorg.zombies.change.UserStopMovingChange;
import com.zorg.zombies.model.MoveDirection;
import com.zorg.zombies.model.User;
import org.springframework.stereotype.Component;

import static com.zorg.zombies.model.MoveDirectionX.*;
import static com.zorg.zombies.model.MoveDirectionY.*;
import static com.zorg.zombies.model.MoveDirectionZ.*;

@Component
public class UserUpdater {

    public UserChange updateUserMove(User user, MoveDirection userMoveDirection) {
        final String userId = user.getId();

        if (NORTH.equals(userMoveDirection)) return changeUserMove(user, NORTH, SOUTH);
        else if (EAST.equals(userMoveDirection)) return changeUserMove(user, EAST, WEST);
        else if (SOUTH.equals(userMoveDirection)) return changeUserMove(user, SOUTH, NORTH);
        else if (WEST.equals(userMoveDirection)) return changeUserMove(user, WEST, EAST);
        else if (UP.equals(userMoveDirection)) return changeUserMove(user, UP, DOWN);
        else if (DOWN.equals(userMoveDirection)) return changeUserMove(user, DOWN, UP);
        else return new NoUserChange(userId);
    }

    private UserChange changeUserMove(User user, MoveDirection move, MoveDirection oppositeMove) {
        final String userId = user.getId();

        if (user.isMoving(move)) return new NoUserChange(userId);
        if (user.isMoving(oppositeMove)) {
            user.setStopMoving(oppositeMove);
            return new UserStopMovingChange(userId, oppositeMove);
        }

        user.setMoving(move);

        return new UserMovingChange(userId, move);
    }

    public UserChange updateUserStopMove(User user, MoveDirection userStopMoveDirection) {
        final String userId = user.getId();

        if ((NORTH.equals(userStopMoveDirection) && !user.isMovingNorth())
                || (EAST.equals(userStopMoveDirection) && !user.isMovingEast())
                || (SOUTH.equals(userStopMoveDirection) && !user.isMovingSouth())
                || (WEST.equals(userStopMoveDirection) && !user.isMovingWest())
                || (UP.equals(userStopMoveDirection) && !user.isMovingUp())
                || (DOWN.equals(userStopMoveDirection) && !user.isMovingDown()))
            return new NoUserChange(userId);

        user.setStopMoving(userStopMoveDirection);

        return new UserStopMovingChange(userId, userStopMoveDirection);
    }
}
