package com.zorg.zombies.service;

import com.zorg.zombies.change.NoUserChange;
import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserMovingChange;
import com.zorg.zombies.change.UserStopMovingChange;
import com.zorg.zombies.model.User;
import com.zorg.zombies.model.exception.WrongDirectionException;
import com.zorg.zombies.model.geometry.Direction;
import lombok.extern.log4j.Log4j2;

import java.util.function.Consumer;

@Log4j2
public class UserUpdater {

    public UserChange updateUserMove(User user, Direction move) {
         String userId = user.getId();

        if (user.isMoving(move)) {
            return new NoUserChange(userId);
        }

//        Direction oppositeMove = move.getOpposite();
//
//        if (isMoving(oppositeMove, user)) {
//            setStopMoving(oppositeMove, user);
//            return new UserStopMovingChange(userId, oppositeMove);
//        }

        setMoving(move, user);

        return new UserMovingChange(userId, move);
    }

    public UserChange updateUserStopMove(User user, Direction userStopMoveDirection) {
        String userId = user.getId();

//        if ((NORTH.equals(userStopMoveDirection) && !user.isMovingNorth())
//                || (EAST.equals(userStopMoveDirection) && !user.isMovingEast())
//                || (SOUTH.equals(userStopMoveDirection) && !user.isMovingSouth())
//                || (WEST.equals(userStopMoveDirection) && !user.isMovingWest())
//                || (UP.equals(userStopMoveDirection) && !user.isMovingUp())
//                || (DOWN.equals(userStopMoveDirection) && !user.isMovingDown())) {
//            return new NoUserChange(userId);
//        }

        setStopMoving(userStopMoveDirection, user);

        return new UserStopMovingChange(userId, userStopMoveDirection);
    }


    void setMoving(Direction direction, User user) {
        getMovingToggle(direction, user).accept(true);
    }

    void setStopMoving(Direction stopMoving, User user) {
        getMovingToggle(stopMoving, user).accept(false);
    }

    Consumer<Boolean> getMovingToggle(Direction direction, User user) {
        switch (direction) {
            case NORTH:
                return user::setWantToMoveNorth;
            case EAST:
                return user::setWantToMoveEast;
            case SOUTH:
                return user::setWantToMoveSouth;
            case WEST:
                return user::setWantToMoveWest;
            case UP:
                return user::setWantToMoveUp;
            case DOWN:
                return user::setWantToMoveDown;
            default:
                throw new WrongDirectionException(direction);
        }
    }
}
