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
        user.setWantToMoveSouth(true);

        var userChange = userUpdater.updateUserMove(user, moveDirection);

        assertTrue(user.isMovingSouth());
        assertFalse(userChange.isUpdate());
    }

    @Test
    void updateUserStopMove_When_UserAlreadyMovingInThatDirection_Expect_StoppedAndUpdated() {
        var moveDirection = SOUTH;
        var user = new User("id", mock(UsersCommunicator.class));
        user.setWantToMoveSouth(true);

        var userChange = userUpdater.updateUserStopMove(user, moveDirection);

        assertFalse(user.isMovingSouth());
        assertTrue(userChange.isUpdate());
        assertTrue(userChange instanceof UserStopMovingChange);

        var userStopMovingChange = (UserStopMovingChange) userChange;

        assertEquals(userStopMovingChange.getStopMoveDirection(), moveDirection);
    }

    @Test
    void updateUserStopMove_When_UserNotMovingInThatDirection_Expect_NotStopped() {
        var moveDirection = SOUTH;
        var user = new User("id", mock(UsersCommunicator.class));

        var userChange = userUpdater.updateUserStopMove(user, moveDirection);

        assertFalse(user.isMoving(moveDirection));
        assertTrue(userChange.isUpdate());
        assertTrue(userChange instanceof UserStopMovingChange);

        var userStopMovingChange = (UserStopMovingChange) userChange;

        assertEquals(userStopMovingChange.getStopMoveDirection(), moveDirection);
    }

    @Test
    void updateUserMove_When_UserMoves_AD_StopMoves_DA_Expect_UserStopped() {
        var userAlreadyMoves = WEST;
        var userNextMove = EAST;
        var user = new User("id", mock(UsersCommunicator.class));

        assertFalse(user.isMoving(userAlreadyMoves));
        assertFalse(user.isMoving(userNextMove));
        userUpdater.setMoving(userAlreadyMoves, user);
        assertTrue(user.isMoving(userAlreadyMoves));
        assertFalse(user.isMoving(userNextMove));
        userUpdater.updateUserMove(user, userNextMove);
        assertFalse(user.isMoving(userAlreadyMoves));
        assertFalse(user.isMoving(userNextMove));
        userUpdater.updateUserStopMove(user, userNextMove);
        assertTrue(user.isMoving(userAlreadyMoves));
        assertFalse(user.isMoving(userNextMove));
        userUpdater.updateUserStopMove(user, userAlreadyMoves);
        assertFalse(user.isMoving(userAlreadyMoves));
        assertFalse(user.isMoving(userNextMove));
    }

    @Test
    void moveScenario() {
        User user = new User("id", mock(UsersCommunicator.class));

        BiFunction<User, Direction, UserChange> updateMove = userUpdater::updateUserMove;
        BiFunction<User, Direction, UserChange> updateStopMove = userUpdater::updateUserStopMove;

        List<Pair<BiFunction<User, Direction, UserChange>, Direction>> scenario = List.of(
                new Pair<>(updateMove, WEST),
                new Pair<>(updateMove, SOUTH),
                new Pair<>(updateStopMove, WEST),
                new Pair<>(updateMove, EAST),
                new Pair<>(updateStopMove, SOUTH),
                new Pair<>(updateMove, NORTH),
                new Pair<>(updateStopMove, EAST),
                new Pair<>(updateStopMove, NORTH)
        );

        BiConsumer<UserChange, Direction> checkUserMoveChange = (userChange, moveDirection) -> {
            assertTrue(user.isMoving(moveDirection));
            assertTrue(userChange.isUpdate());
            assertTrue(userChange instanceof UserMovingChange);

            var userMovingChange = (UserMovingChange) userChange;

            assertEquals(userMovingChange.getMoveDirection(), moveDirection);
        };

        BiConsumer<UserChange, Direction> checkUserStopMoveChange = (userChange, moveDirection) -> {
            assertFalse(user.isMoving(moveDirection));
            assertTrue(userChange.isUpdate());
            assertTrue(userChange instanceof UserStopMovingChange);

            var userStopMovingChange = (UserStopMovingChange) userChange;

            assertEquals(userStopMovingChange.getStopMoveDirection(), moveDirection);
        };

        List<BiConsumer<UserChange, Direction>> expects = List.of(
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
            Pair<BiFunction<User, Direction, UserChange>, Direction> entry = scenario.get(i);
            BiFunction<User, Direction, UserChange> changeBiFunction = entry.getKey();
            Direction direction = entry.getValue();

            BiConsumer<UserChange, Direction> directionBiConsumer = expects.get(i);
            UserChange userChange = changeBiFunction.apply(user, direction);

            directionBiConsumer.accept(userChange, direction);
        }
    }
}
