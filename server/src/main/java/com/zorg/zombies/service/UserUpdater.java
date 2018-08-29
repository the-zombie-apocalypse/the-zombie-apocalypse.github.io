package com.zorg.zombies.service;

import com.zorg.zombies.change.NoUserChange;
import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserMovingChange;
import com.zorg.zombies.model.MoveDirection;
import com.zorg.zombies.model.User;
import org.springframework.stereotype.Component;

import static com.zorg.zombies.model.MoveDirectionX.*;
import static com.zorg.zombies.model.MoveDirectionY.*;
import static com.zorg.zombies.model.MoveDirectionZ.*;

@Component
public class UserUpdater {

    public UserChange updateUserMove(User user, MoveDirection userMoveDirection) {
        // todo: implement stopping user move in case of opposite moves

        if (NORTH.equals(userMoveDirection)) {
            if (user.isMovingNorth()) return new NoUserChange(user);
            user.setMovingNorth(true);

        } else if (EAST.equals(userMoveDirection)) {
            if (user.isMovingEast()) return new NoUserChange(user);
            user.setMovingEast(true);

        } else if (SOUTH.equals(userMoveDirection)) {
            if (user.isMovingSouth()) return new NoUserChange(user);
            user.setMovingSouth(true);

        } else if (WEST.equals(userMoveDirection)) {
            if (user.isMovingWest()) return new NoUserChange(user);
            user.setMovingWest(true);

        } else if (UP.equals(userMoveDirection)) {
            if (user.isMovingUp()) return new NoUserChange(user);
            user.setMovingUp(true);

        } else if (DOWN.equals(userMoveDirection)) {
            if (user.isMovingDown()) return new NoUserChange(user);
            user.setMovingDown(true);
        }

        return new UserMovingChange(user, userMoveDirection);
    }

    public UserChange updateUserStopMove(User user, MoveDirection userStopMoveDirection) {
        if (NORTH.equals(userStopMoveDirection)) {
            if (!user.isMovingNorth()) return new NoUserChange(user);
            user.setMovingNorth(false);

        } else if (EAST.equals(userStopMoveDirection)) {
            if (!user.isMovingEast()) return new NoUserChange(user);
            user.setMovingEast(false);

        } else if (SOUTH.equals(userStopMoveDirection)) {
            if (!user.isMovingSouth()) return new NoUserChange(user);
            user.setMovingSouth(false);

        } else if (WEST.equals(userStopMoveDirection)) {
            if (!user.isMovingWest()) return new NoUserChange(user);
            user.setMovingWest(false);

        } else if (UP.equals(userStopMoveDirection)) {
            if (!user.isMovingUp()) return new NoUserChange(user);
            user.setMovingUp(false);

        } else if (DOWN.equals(userStopMoveDirection)) {
            if (!user.isMovingDown()) return new NoUserChange(user);
            user.setMovingDown(false);
        }

        return new UserMovingChange(user, userStopMoveDirection);
    }
}
