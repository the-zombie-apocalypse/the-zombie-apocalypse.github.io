package com.zorg.zombies.service;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserMovingChange;
import com.zorg.zombies.change.UserStopMovingChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.change.WorldOnLoad;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.model.MoveDirection;
import com.zorg.zombies.model.MoveDirectionY;
import com.zorg.zombies.model.User;
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

import static com.zorg.zombies.model.MoveDirectionX.*;
import static com.zorg.zombies.model.MoveDirectionY.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GameSupervisorTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    private static final String SESSION_ID = "id";

    @MockBean
    private UserService userService;

    @Autowired
    private GameSupervisor gameSupervisor;

    @BeforeEach
    void setUp() {
        BDDMockito.given(userService.createUser(SESSION_ID)).willReturn(new User(SESSION_ID) {
            {
                movementNotifierEnabled = false;
            }
        });
    }

    @Test
    void createGameActionsProcessor_When_SomeId_Expect_NotNullReturned() {
        final FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
        assertNotNull(processor);
    }

    @Test
    void createGameActionsProcessor_When_IdGiven_Expect_FirstMessageIsGreeting() {
        final FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
        processor.onComplete();

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
        final MoveDirectionY direction = NORTH;
        final Command userMovingNorthCommand = new UserMoveCommand(SESSION_ID, direction);
        final FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
        processor.onNext(userMovingNorthCommand);
        processor.onComplete();

        StepVerifier.create(processor)
                .assertNext(getGreetingAssertion(SESSION_ID)).as("check greeting")
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, direction))
                .as("check user move command")
                .expectComplete()
                .log()
                .verify(TIMEOUT);
    }

    private Consumer<? super WorldChange> getUserMovingChangeAssertion(final String userId, final MoveDirection direction) {
        return worldChange -> {
            System.out.println(worldChange);
            final UserChange userChange = worldChange.getUser();
            assertEquals(userChange.getId(), userId);

            assertTrue(userChange instanceof UserMovingChange);
            final UserMovingChange userMovingChange = (UserMovingChange) userChange;
            assertTrue(userMovingChange.isUpdated());
            assertEquals(userMovingChange.getMoveDirection(), direction);
        };
    }

    @Test
    void reactionOnCommand_When_UserStartMovingUpAndStopMovingUp_Expect_UserMovingUpThenUserStopMovingUpResponse() {
        final MoveDirectionY direction = NORTH;
        final Command userMovingNorthCommand = new UserMoveCommand(SESSION_ID, direction);
        final Command userStopMovingNorthCommand = new UserStopMoveCommand(SESSION_ID, direction);

        final FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
        processor.onNext(userMovingNorthCommand);
        processor.onNext(userStopMovingNorthCommand);
        processor.onComplete();

        StepVerifier.create(processor)
                .assertNext(getGreetingAssertion(SESSION_ID)).as("check greeting")
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, direction))
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, direction))
                .as("check user stop move command")
                .expectComplete()
                .log()
                .verify(TIMEOUT);
    }

    private Consumer<? super WorldChange> getUserStopMovingChangeAssertion(final String userId, final MoveDirection direction) {
        return worldChange -> {
            System.out.println(worldChange);
            final UserChange userChange = worldChange.getUser();
            assertEquals(userChange.getId(), userId);

            assertTrue(userChange instanceof UserStopMovingChange);
            final UserStopMovingChange stopMovingChange = (UserStopMovingChange) userChange;

            assertTrue(stopMovingChange.isUpdated());
            assertEquals(stopMovingChange.getStopMoveDirection(), direction);
        };
    }

    @Test
    void reactionOnCommand_When_UserIsMovingAndStoppingManyTimes_Expect_CorrectWorldChanges() {
        final Command userMovingNorthCommand = new UserMoveCommand(SESSION_ID, NORTH);
        final Command userStopMovingNorthCommand = new UserStopMoveCommand(SESSION_ID, NORTH);

        final Command userMovingSouthCommand = new UserMoveCommand(SESSION_ID, SOUTH);
        final Command userStopMovingSouthCommand = new UserStopMoveCommand(SESSION_ID, SOUTH);

        final Command userMovingWestCommand = new UserMoveCommand(SESSION_ID, WEST);
        final Command userStopMovingWestCommand = new UserStopMoveCommand(SESSION_ID, WEST);

        final Command userMovingEastCommand = new UserMoveCommand(SESSION_ID, EAST);
        final Command userStopMovingEastCommand = new UserStopMoveCommand(SESSION_ID, EAST);

        final FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
        processor.onNext(userMovingNorthCommand);
        processor.onNext(userMovingWestCommand);
        processor.onNext(userStopMovingNorthCommand);
        processor.onNext(userMovingNorthCommand);
        processor.onNext(userStopMovingNorthCommand);
        processor.onNext(userStopMovingWestCommand);
        processor.onNext(userMovingEastCommand);
        processor.onNext(userMovingSouthCommand);
        processor.onNext(userStopMovingSouthCommand);
        processor.onNext(userStopMovingEastCommand);
        processor.onComplete();

        StepVerifier.create(processor)
                .assertNext(getGreetingAssertion(SESSION_ID)).as("check greeting")
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, NORTH))
                .as("check user move north command")
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, WEST))
                .as("check user move west command")
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, NORTH))
                .as("check user stop move north command")
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, NORTH))
                .as("check user move north command")
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, NORTH))
                .as("check user stop move north command")
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, WEST))
                .as("check user stop move west command")
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, EAST))
                .as("check user move east command")
                .assertNext(getUserMovingChangeAssertion(SESSION_ID, SOUTH))
                .as("check user move south command")
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, SOUTH))
                .as("check user stop move south command")
                .assertNext(getUserStopMovingChangeAssertion(SESSION_ID, EAST))
                .as("check user stop move east command")
                .expectComplete()
                .log()
                .verify(TIMEOUT);
    }
}
