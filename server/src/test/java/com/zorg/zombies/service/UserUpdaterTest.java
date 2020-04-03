package com.zorg.zombies.service;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserMovingChange;
import com.zorg.zombies.change.UserStopMovingChange;
import com.zorg.zombies.model.User;
import com.zorg.zombies.model.geometry.Direction;
import com.zorg.zombies.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static com.zorg.zombies.model.geometry.Direction.DOWN;
import static com.zorg.zombies.model.geometry.Direction.EAST;
import static com.zorg.zombies.model.geometry.Direction.NORTH;
import static com.zorg.zombies.model.geometry.Direction.SOUTH;
import static com.zorg.zombies.model.geometry.Direction.UP;
import static com.zorg.zombies.model.geometry.Direction.WEST;
import static com.zorg.zombies.util.MovementAndDirections.isMoving;
import static com.zorg.zombies.util.MovementAndDirections.setMoving;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserUpdaterTest {

    @InjectMocks
    private UserUpdater userUpdater;

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingNorth_Expect_Updated() {
        var user = new User("id", mock(UsersCommunicator.class));
        var moveDirection = NORTH;

        assertFalse(user.isMovingNorth());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingNorth());
        assertTrue(userChange.isUpdate());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingEast_Expect_Updated() {
        var user = new User("id", mock(UsersCommunicator.class));
        var moveDirection = EAST;

        assertFalse(user.isMovingEast());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingEast());
        assertTrue(userChange.isUpdate());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingSouth_Expect_Updated() {
        var user = new User("id", mock(UsersCommunicator.class));
        var moveDirection = SOUTH;

        assertFalse(user.isMovingSouth());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingSouth());
        assertTrue(userChange.isUpdate());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingWest_Expect_Updated() {
        var user = new User("id", mock(UsersCommunicator.class));
        var moveDirection = WEST;

        assertFalse(user.isMovingWest());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingWest());
        assertTrue(userChange.isUpdate());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingUp_Expect_Updated() {
        var user = new User("id", mock(UsersCommunicator.class));
        var moveDirection = UP;

        assertFalse(user.isMovingUp());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingUp());
        assertTrue(userChange.isUpdate());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserNotMovingAndThenMovingDown_Expect_Updated() {
        var user = new User("id", mock(UsersCommunicator.class));
        var moveDirection = DOWN;

        assertFalse(user.isMovingDown());

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingDown());
        assertTrue(userChange.isUpdate());
        assertTrue(userChange instanceof UserMovingChange);

        var userMovingChange = (UserMovingChange) userChange;

        assertEquals(userMovingChange.getMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserAlreadyMovingInThatDirection_Expect_NotUpdated() {
        var moveDirection = SOUTH;
        var user = new User("id", mock(UsersCommunicator.class));
        user.setMovingSouth(true);

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingSouth());
        assertFalse(userChange.isUpdate());
    }

    @Test
    void updateUserStopMove_When_UserAlreadyMovingInThatDirection_Expect_StoppedAndUpdated() {
        var moveDirection = SOUTH;
        var user = new User("id", mock(UsersCommunicator.class));
        user.setMovingSouth(true);

        var userChange = userUpdater.updateUserStopMove(user, moveDirection);

        assertFalse(user.isMovingSouth());
        assertTrue(userChange.isUpdate());
        assertTrue(userChange instanceof UserStopMovingChange);

        var userStopMovingChange = (UserStopMovingChange) userChange;

        assertEquals(userStopMovingChange.getStopMoveDirection(), moveDirection);
    }

    @Test
    void updateUserStopMove_When_UserNotMovingInThatDirection_Expect_NotStoppedAndNotUpdated() {
        var moveDirection = SOUTH;
        var user = new User("id", mock(UsersCommunicator.class));

        var userChange = userUpdater.updateUserStopMove(user, moveDirection);

        assertFalse(user.isMovingSouth());
        assertFalse(userChange.isUpdate());
    }

    @Test
    void updateUserMove_When_UserAlreadyMovesWestAndThenMovingEast_Expect_UserStopMovingWestAndNotMovingEast() {
        var userAlreadyMoves = WEST;
        var userNextMove = EAST;
        var user = new User("id", mock(UsersCommunicator.class));

        setMoving(userAlreadyMoves, user);

        var userChange = userUpdater.updateUserMove(user, userNextMove);

        assertFalse(isMoving(userNextMove, user));
        assertFalse(isMoving(userAlreadyMoves, user));
        assertTrue(userChange.isUpdate());
        assertTrue(userChange instanceof UserStopMovingChange);

        var userStopMovingChange = (UserStopMovingChange) userChange;

        assertEquals(userStopMovingChange.getStopMoveDirection(), userAlreadyMoves);
    }

    @Test
    void moveScenario() {
        User user = new User("id", mock(UsersCommunicator.class));

        BiFunction<User, Direction, UserChange> updateMove = userUpdater::updateUserMove;
        BiFunction<User, Direction, UserChange> updateStopMove = userUpdater::updateUserStopMove;

        final List<Pair<BiFunction<User, Direction, UserChange>, Direction>> scenario = List.of(
                new Pair<>(updateMove, WEST),
                new Pair<>(updateMove, SOUTH),
                new Pair<>(updateStopMove, WEST),
                new Pair<>(updateMove, EAST),
                new Pair<>(updateStopMove, SOUTH),
                new Pair<>(updateMove, NORTH),
                new Pair<>(updateStopMove, EAST),
                new Pair<>(updateStopMove, NORTH)
        );

        final BiConsumer<UserChange, Direction> checkUserMoveChange = (userChange, moveDirection) -> {
            assertTrue(isMoving(moveDirection, user));
            assertTrue(userChange.isUpdate());
            assertTrue(userChange instanceof UserMovingChange);

            var userMovingChange = (UserMovingChange) userChange;

            assertEquals(userMovingChange.getMoveDirection(), moveDirection);
        };

        final BiConsumer<UserChange, Direction> checkUserStopMoveChange = (userChange, moveDirection) -> {
            assertFalse(isMoving(moveDirection, user));
            assertTrue(userChange.isUpdate());
            assertTrue(userChange instanceof UserStopMovingChange);

            var userStopMovingChange = (UserStopMovingChange) userChange;

            assertEquals(userStopMovingChange.getStopMoveDirection(), moveDirection);
        };

        final List<BiConsumer<UserChange, Direction>> expects = List.of(
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
            final Pair<BiFunction<User, Direction, UserChange>, Direction> entry = scenario.get(i);
            final BiFunction<User, Direction, UserChange> changeBiFunction = entry.getKey();
            final Direction direction = entry.getValue();

            final BiConsumer<UserChange, Direction> directionBiConsumer = expects.get(i);
            final UserChange userChange = changeBiFunction.apply(user, direction);

            directionBiConsumer.accept(userChange, direction);
        }
    }
}
