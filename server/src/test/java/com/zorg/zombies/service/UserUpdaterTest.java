package com.zorg.zombies.service;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserMovingChange;
import com.zorg.zombies.change.UserStopMovingChange;
import com.zorg.zombies.model.MoveDirection;
import com.zorg.zombies.model.MoveDirectionX;
import com.zorg.zombies.model.MoveDirectionY;
import com.zorg.zombies.model.MoveDirectionZ;
import com.zorg.zombies.model.User;
import com.zorg.zombies.util.Pair;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static com.zorg.zombies.util.MovementAndDirections.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUpdaterTest {

    @InjectMocks
    private UserUpdater userUpdater;

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingNorth_Expect_Updated() {
        var user = new User("id", mock(UsersCommunicator.class));
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
        var user = new User("id", mock(UsersCommunicator.class));
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
        var user = new User("id", mock(UsersCommunicator.class));
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
        var user = new User("id", mock(UsersCommunicator.class));
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
        var user = new User("id", mock(UsersCommunicator.class));
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
        var user = new User("id", mock(UsersCommunicator.class));
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
        var user = new User("id", mock(UsersCommunicator.class));
        user.setMovingSouth(true);

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingSouth());
        assertFalse(userChange.isUpdated());
    }

    @Test
    void updateUserStopMove_When_UserAlreadyMovingInThatDirection_Expect_StoppedAndUpdated() {
        var moveDirection = MoveDirectionY.SOUTH;
        var user = new User("id", mock(UsersCommunicator.class));
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
        var user = new User("id", mock(UsersCommunicator.class));

        var userChange = userUpdater.updateUserStopMove(user, moveDirection);

        assertFalse(user.isMovingSouth());
        assertFalse(userChange.isUpdated());
    }

    @Test
    void updateUserMove_When_UserAlreadyMovesWestAndThenMovingEast_Expect_UserStopMovingWestAndNotMovingEast() {
        var userAlreadyMoves = MoveDirectionX.WEST;
        var userNextMove = MoveDirectionX.EAST;
        var user = new User("id", mock(UsersCommunicator.class));

        setMoving(userAlreadyMoves, user);

        var userChange = userUpdater.updateUserMove(user, userNextMove);

        assertFalse(isMoving(userNextMove, user));
        assertFalse(isMoving(userAlreadyMoves, user));
        assertTrue(userChange.isUpdated());
        assertTrue(userChange instanceof UserStopMovingChange);

        var userStopMovingChange = (UserStopMovingChange) userChange;

        assertEquals(userStopMovingChange.getStopMoveDirection(), userAlreadyMoves);
    }

    @Test
    void moveScenario() {
        val user = new User("id", mock(UsersCommunicator.class));

        BiFunction<User, MoveDirection, UserChange> updateMove = userUpdater::updateUserMove;
        BiFunction<User, MoveDirection, UserChange> updateStopMove = userUpdater::updateUserStopMove;

        final List<Pair<BiFunction<User, MoveDirection, UserChange>, MoveDirection>> scenario = List.of(
                new Pair<>(updateMove, MoveDirectionX.WEST),
                new Pair<>(updateMove, MoveDirectionY.SOUTH),
                new Pair<>(updateStopMove, MoveDirectionX.WEST),
                new Pair<>(updateMove, MoveDirectionX.EAST),
                new Pair<>(updateStopMove, MoveDirectionY.SOUTH),
                new Pair<>(updateMove, MoveDirectionY.NORTH),
                new Pair<>(updateStopMove, MoveDirectionX.EAST),
                new Pair<>(updateStopMove, MoveDirectionY.NORTH)
        );

        final BiConsumer<UserChange, MoveDirection> checkUserMoveChange = (userChange, moveDirection) -> {
            assertTrue(isMoving(moveDirection, user));
            assertTrue(userChange.isUpdated());
            assertTrue(userChange instanceof UserMovingChange);

            var userMovingChange = (UserMovingChange) userChange;

            assertEquals(userMovingChange.getMoveDirection(), moveDirection);
        };

        final BiConsumer<UserChange, MoveDirection> checkUserStopMoveChange = (userChange, moveDirection) -> {
            assertFalse(isMoving(moveDirection, user));
            assertTrue(userChange.isUpdated());
            assertTrue(userChange instanceof UserStopMovingChange);

            var userStopMovingChange = (UserStopMovingChange) userChange;

            assertEquals(userStopMovingChange.getStopMoveDirection(), moveDirection);
        };

        final List<BiConsumer<UserChange, MoveDirection>> expects = List.of(
                checkUserMoveChange,
                checkUserMoveChange,
                checkUserStopMoveChange,
                checkUserMoveChange,
                checkUserStopMoveChange,
                checkUserMoveChange,
                checkUserStopMoveChange,
                checkUserStopMoveChange
        );

        for (int i = 0; i < scenario.size(); i++) {
            final Pair<BiFunction<User, MoveDirection, UserChange>, MoveDirection> entry = scenario.get(i);
            final BiFunction<User, MoveDirection, UserChange> changeBiFunction = entry.getKey();
            final MoveDirection direction = entry.getValue();

            final BiConsumer<UserChange, MoveDirection> directionBiConsumer = expects.get(i);
            final UserChange userChange = changeBiFunction.apply(user, direction);

            directionBiConsumer.accept(userChange, direction);
        }
    }
}
