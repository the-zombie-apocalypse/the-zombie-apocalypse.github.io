package com.zorg.zombies.service;

import com.zorg.zombies.change.NoUserChange;
import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserMovingChange;
import com.zorg.zombies.change.UserStopMovingChange;
import com.zorg.zombies.model.User;
import com.zorg.zombies.model.geometry.Direction;

import static com.zorg.zombies.model.geometry.Direction.DOWN;
import static com.zorg.zombies.model.geometry.Direction.EAST;
import static com.zorg.zombies.model.geometry.Direction.NORTH;
import static com.zorg.zombies.model.geometry.Direction.SOUTH;
import static com.zorg.zombies.model.geometry.Direction.UP;
import static com.zorg.zombies.model.geometry.Direction.WEST;
import static com.zorg.zombies.util.MovementAndDirections.isMoving;
import static com.zorg.zombies.util.MovementAndDirections.setMoving;
import static com.zorg.zombies.util.MovementAndDirections.setStopMoving;

public class UserUpdater {

    public UserChange updateUserMove(User user, Direction move) {
        final String userId = user.getId();

        if (isMoving(move, user)) {
            return new NoUserChange(userId);
        }

        Direction oppositeMove = move.getOpposite();

        if (isMoving(oppositeMove, user)) {
            setStopMoving(oppositeMove, user);
            return new UserStopMovingChange(userId, oppositeMove);
        }

        setMoving(move, user);

        return new UserMovingChange(userId, move);
    }

    public UserChange updateUserStopMove(User user, Direction userStopMoveDirection) {
        final String userId = user.getId();

        if ((NORTH.equals(userStopMoveDirection) && !user.isMovingNorth())
                || (EAST.equals(userStopMoveDirection) && !user.isMovingEast())
                || (SOUTH.equals(userStopMoveDirection) && !user.isMovingSouth())
                || (WEST.equals(userStopMoveDirection) && !user.isMovingWest())
                || (UP.equals(userStopMoveDirection) && !user.isMovingUp())
                || (DOWN.equals(userStopMoveDirection) && !user.isMovingDown())) {
            return new NoUserChange(userId);
        }

        setStopMoving(userStopMoveDirection, user);

        return new UserStopMovingChange(userId, userStopMoveDirection);
    }
}
