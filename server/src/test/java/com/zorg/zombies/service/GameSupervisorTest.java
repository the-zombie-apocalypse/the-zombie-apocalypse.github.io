package com.zorg.zombies.service;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserMovingChange;
import com.zorg.zombies.change.UserStopMovingChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.change.WorldOnLoad;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStartGameCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.model.User;
import com.zorg.zombies.model.geometry.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.FluxProcessor;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.function.Consumer;

import static com.zorg.zombies.model.geometry.Direction.EAST;
import static com.zorg.zombies.model.geometry.Direction.NORTH;
import static com.zorg.zombies.model.geometry.Direction.SOUTH;
import static com.zorg.zombies.model.geometry.Direction.WEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GameSupervisorTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(3);
    private static final String SESSION_ID = "id";

    @MockBean
    private UserService userService;

    @Autowired
    private UsersCommunicator usersCommunicator;

    @Autowired
    private GameSupervisor gameSupervisor;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(SESSION_ID, usersCommunicator);
        user.setMovementNotifierEnabled(false);

        BDDMockito.given(userService.createUser(SESSION_ID)).willReturn(user);
    }

//    @AfterEach
//    void tearDown() {
//        user.getSubscriber().onComplete();
//    }

    @Test
    void createGameActionsProcessor_When_SomeId_Expect_NotNullReturned() {
        FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
        assertNotNull(processor);
    }

    @Test
    void createGameActionsProcessor_When_IdGiven_Expect_FirstMessageIsGreeting() {
        FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
        processor.onNext(new UserStartGameCommand("nickname"));
        processor.onComplete();
        user.getSubscriber().onComplete();

        StepVerifier.create(processor)
                .assertNext(getGreetingAssertion(SESSION_ID))
                .expectComplete()
                .log()
                .verify(TIMEOUT);
    }

    private Consumer<WorldChange> getGreetingAssertion(String id) {
        return worldChange -> {
            System.out.println("Greeting: " + worldChange);
            assertTrue(worldChange instanceof WorldOnLoad);
            assertEquals(worldChange.getUser().getId(), id);
        };
    }

    @Test
    void reactionOnCommand_When_UserStartMovingUp_Expect_UserMovingUpResponse() {
        Direction direction = NORTH;
        Command userMovingNorthCommand = new UserMoveCommand(direction);
        FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
        processor.onNext(new UserStartGameCommand("nickname"));
        processor.onNext(userMovingNorthCommand);
        processor.onComplete();
        user.getSubscriber().onComplete();

        StepVerifier.create(processor)
                .assertNext(getGreetingAssertion(SESSION_ID)).as("check greeting")
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, direction))
                .as("check user move command")
                .expectComplete()
                .log()
                .verify(TIMEOUT);
    }

    private Consumer<? super WorldChange> getUserMovingChangeAssertion(final String userId, final Direction direction) {
        return worldChange -> {
            System.out.println(worldChange);
            UserChange userChange = worldChange.getUser();
            assertEquals(userChange.getId(), userId);

            assertTrue(userChange instanceof UserMovingChange);
            UserMovingChange userMovingChange = (UserMovingChange) userChange;
            assertTrue(userMovingChange.isUpdate());
            assertEquals(userMovingChange.getMoveDirection(), direction);
        };
    }

    @Test
    void reactionOnCommand_When_UserStartMovingUpAndStopMovingUp_Expect_UserMovingUpThenUserStopMovingUpResponse() {
        Direction direction = NORTH;
        Command userMovingNorthCommand = new UserMoveCommand(direction);
        Command userStopMovingNorthCommand = new UserStopMoveCommand(direction);

        FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
        processor.onNext(new UserStartGameCommand("nickname"));
        processor.onNext(userMovingNorthCommand);
        processor.onNext(userStopMovingNorthCommand);
        processor.onComplete();
        user.getSubscriber().onComplete();

        StepVerifier.create(processor)
                .assertNext(getGreetingAssertion(SESSION_ID)).as("check greeting")
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, direction))
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, direction))
                .as("check user stop move command")
                .expectComplete()
                .log()
                .verify(TIMEOUT);
    }

    private Consumer<? super WorldChange> getUserStopMovingChangeAssertion(final String userId, final Direction direction) {
        return worldChange -> {
            System.out.println(worldChange);
            UserChange userChange = worldChange.getUser();
            assertEquals(userChange.getId(), userId);

            assertTrue(userChange instanceof UserStopMovingChange);
            UserStopMovingChange stopMovingChange = (UserStopMovingChange) userChange;

            assertTrue(stopMovingChange.isUpdate());
            assertEquals(stopMovingChange.getStopMoveDirection(), direction);
        };
    }

    @Test
    void reactionOnCommand_When_UserIsMovingAndStoppingManyTimes_Expect_CorrectWorldChanges() {
        Command userMovingNorthCommand = new UserMoveCommand(NORTH);
        Command userStopMovingNorthCommand = new UserStopMoveCommand(NORTH);

        Command userMovingSouthCommand = new UserMoveCommand(SOUTH);
        Command userStopMovingSouthCommand = new UserStopMoveCommand(SOUTH);

        Command userMovingWestCommand = new UserMoveCommand(WEST);
        Command userStopMovingWestCommand = new UserStopMoveCommand(WEST);

        Command userMovingEastCommand = new UserMoveCommand(EAST);
        Command userStopMovingEastCommand = new UserStopMoveCommand(EAST);

        FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);

        StepVerifier.create(processor)
                .then(() -> processor.onNext(new UserStartGameCommand("nickname")))
                .assertNext(getGreetingAssertion(SESSION_ID)).as("check greeting")
                .then(() -> processor.onNext(userMovingNorthCommand))
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, NORTH))
                .as("check user move north command")
                .then(() -> processor.onNext(userMovingWestCommand))
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, WEST))
                .as("check user move west command")
                .then(() -> processor.onNext(userStopMovingNorthCommand))
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, NORTH))
                .as("check user stop move north command")
                .then(() -> processor.onNext(userMovingNorthCommand))
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, NORTH))
                .as("check user move north command")
                .then(() -> processor.onNext(userStopMovingNorthCommand))
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, NORTH))
                .as("check user stop move north command")
                .then(() -> processor.onNext(userStopMovingWestCommand))
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, WEST))
                .as("check user stop move west command")
                .then(() -> processor.onNext(userMovingEastCommand))
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, EAST))
                .as("check user move east command")
                .then(() -> processor.onNext(userMovingSouthCommand))
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, SOUTH))
                .as("check user move south command")
                .then(() -> processor.onNext(userStopMovingSouthCommand))
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, SOUTH))
                .as("check user stop move south command")
                .then(() -> processor.onNext(userStopMovingEastCommand))
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, EAST))
                .as("check user stop move east command")
                .then(() -> {
                    processor.onComplete();
                    user.getSubscriber().onComplete();
                })
                .expectComplete()
                .log()
                .verify(TIMEOUT);
    }
}
