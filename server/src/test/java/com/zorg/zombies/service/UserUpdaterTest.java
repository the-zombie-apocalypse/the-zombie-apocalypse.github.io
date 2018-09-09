package com.zorg.zombies.service;

import com.zorg.zombies.change.UserMovingChange;
import com.zorg.zombies.change.UserStopMovingChange;
import com.zorg.zombies.model.MoveDirectionX;
import com.zorg.zombies.model.MoveDirectionY;
import com.zorg.zombies.model.MoveDirectionZ;
import com.zorg.zombies.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserUpdaterTest {

    @InjectMocks
    private UserUpdater userUpdater;

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingNorth_Expect_Updated() {
        var user = new User("id");
        var moveDirection = MoveDirectionY.NORTH;

        assertFalse(user.isMovingNorth());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingNorth());
        assertTrue(userChange.isUpdated());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingEast_Expect_Updated() {
        var user = new User("id");
        var moveDirection = MoveDirectionX.EAST;

        assertFalse(user.isMovingEast());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingEast());
        assertTrue(userChange.isUpdated());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingSouth_Expect_Updated() {
        var user = new User("id");
        var moveDirection = MoveDirectionY.SOUTH;

        assertFalse(user.isMovingSouth());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingSouth());
        assertTrue(userChange.isUpdated());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingWest_Expect_Updated() {
        var user = new User("id");
        var moveDirection = MoveDirectionX.WEST;

        assertFalse(user.isMovingWest());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingWest());
        assertTrue(userChange.isUpdated());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingUp_Expect_Updated() {
        var user = new User("id");
        var moveDirection = MoveDirectionZ.UP;

        assertFalse(user.isMovingUp());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingUp());
        assertTrue(userChange.isUpdated());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingDown_Expect_Updated() {
        var user = new User("id");
        var moveDirection = MoveDirectionZ.DOWN;

        assertFalse(user.isMovingDown());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingDown());
        assertTrue(userChange.isUpdated());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserAlreadyMovingInThatDirection_Expect_NotUpdated() {
        var moveDirection = MoveDirectionY.SOUTH;
        var user = new User("id");
        user.setMovingSouth(true);

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingSouth());
        assertFalse(userChange.isUpdated());
    }

    @Test
    void updateUserStopMove_When_UserAlreadyMovingInThatDirection_Expect_StoppedAndUpdated() {
        var moveDirection = MoveDirectionY.SOUTH;
        var user = new User("id");
        user.setMovingSouth(true);

        var userChange = userUpdater.updateUserStopMove(user, moveDirection);

        assertFalse(user.isMovingSouth());
        assertTrue(userChange.isUpdated());
        assertTrue(userChange instanceof UserStopMovingChange);

        var userStopMovingChange = (UserStopMovingChange) userChange;

        assertEquals(userStopMovingChange.getStopMoveDirection(), moveDirection);
    }

    @Test
    void updateUserStopMove_When_UserNotMovingInThatDirection_Expect_NotStoppedAndNotUpdated() {
        var moveDirection = MoveDirectionY.SOUTH;
        var user = new User("id");

        var userChange = userUpdater.updateUserStopMove(user, moveDirection);

        assertFalse(user.isMovingSouth());
        assertFalse(userChange.isUpdated());
    }

    @Test
    void updateUserMove_When_UserAlreadyMovesWestAndThenMovingEast_Expect_UserStopMovingWestAndNotMovingEast() {
        var userAlreadyMoves = MoveDirectionX.WEST;
        var userNextMove = MoveDirectionX.EAST;

        var user = new User("id");
        user.setMoving(userAlreadyMoves);

        var userChange = userUpdater.updateUserMove(user, userNextMove);

        assertFalse(user.isMoving(userNextMove));
        assertFalse(user.isMoving(userAlreadyMoves));
        assertTrue(userChange.isUpdated());
        assertTrue(userChange instanceof UserStopMovingChange);

        var userStopMovingChange = (UserStopMovingChange) userChange;

        assertEquals(userStopMovingChange.getStopMoveDirection(), userAlreadyMoves);
    }
}
