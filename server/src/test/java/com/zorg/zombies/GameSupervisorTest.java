package com.zorg.zombies;

import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.service.GameSupervisor;
import com.zorg.zombies.service.UserIdDefiner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.FluxProcessor;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GameSupervisorTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    private static final String SESSION_ID = "id";

    @Mock
    private UserIdDefiner userIdDefiner;

    @InjectMocks
    private GameSupervisor gameSupervisor;

    @BeforeEach
    void setUp() {
        given(userIdDefiner.getUserId(SESSION_ID)).willCallRealMethod();
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
            assertEquals(worldChange.getUser().getId(), id);
        };
    }

//    @Test
//    void reactionOnCommand_When_UserStartMovingUp_Expect_UserMovingUpResponse() {
//        final Command userMovingUpCommand = commandWithUser(SESSION_ID, user -> user.setMovingNorth(true));
//        final FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
//        processor.onNext(userMovingUpCommand);
//        processor.onComplete();
//
//        StepVerifier.create(processor)
//                .assertNext(getGreetingAssertion(SESSION_ID)).as("check greeting")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isMovingUp());
//                })
//                .as("check user move command")
//                .expectComplete()
//                .log()
//                .verify(TIMEOUT);
//    }
//
//    @Test
//    void reactionOnCommand_When_UserStartMovingUpAndStopMovingUp_Expect_UserMovingUpThenUserStopMovingUpResponse() {
//        final Command userMovingUpCommand = commandWithUser(SESSION_ID, user -> user.setMovingNorth(true));
//        final Command userStopMovingUpCommand = commandWithUser(SESSION_ID, user -> user.setStopMovingNorth(true));
//
//        final FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
//        processor.onNext(userMovingUpCommand);
//        processor.onNext(userStopMovingUpCommand);
//        processor.onComplete();
//
//        StepVerifier.create(processor)
//                .assertNext(getGreetingAssertion(SESSION_ID)).as("check greeting")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isMovingUp());
//                    assertFalse(worldChange.getUser().isStopMovingUp());
//                })
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isStopMovingUp());
//                    assertFalse(worldChange.getUser().isMovingUp());
//                })
//                .as("check user stop move command")
//                .expectComplete()
//                .log()
//                .verify(TIMEOUT);
//    }
//
//    private Command commandWithUser(String id, Consumer<User> setUserStuff) {
//        final User user = new User(id);
//        setUserStuff.accept(user);
//
//        final Command command = new Command();
//        command.setUser(user);
//
//        return command;
//    }
//
//    @Test
//    void reactionOnCommand_When_UserIsMovingAndStoppingManyTimes_Expect_CorrectWorldChanges() {
//        final Command userMovingUpCommand = commandWithUser(SESSION_ID, user -> user.setMovingNorth(true));
//        final Command userStopMovingUpCommand = commandWithUser(SESSION_ID, user -> user.setStopMovingNorth(true));
//
//        final Command userMovingDownCommand = commandWithUser(SESSION_ID, user -> user.setMovingSouth(true));
//        final Command userStopMovingDownCommand = commandWithUser(SESSION_ID, user -> user.setStopMovingSouth(true));
//
//        final Command userMovingLeftCommand = commandWithUser(SESSION_ID, user -> user.setMovingWest(true));
//        final Command userStopMovingLeftCommand = commandWithUser(SESSION_ID, user -> user.setStopMovingWest(true));
//
//        final Command userMovingRightCommand = commandWithUser(SESSION_ID, user -> user.setMovingEast(true));
//        final Command userStopMovingRightCommand = commandWithUser(SESSION_ID, user -> user.setStopMovingEast(true));
//
//        final FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
//        processor.onNext(userMovingUpCommand);
//        processor.onNext(userMovingLeftCommand);
//        processor.onNext(userStopMovingUpCommand);
//        processor.onNext(userMovingUpCommand);
//        processor.onNext(userStopMovingUpCommand);
//        processor.onNext(userStopMovingLeftCommand);
//        processor.onNext(userMovingRightCommand);
//        processor.onNext(userMovingDownCommand);
//        processor.onNext(userStopMovingDownCommand);
//        processor.onNext(userStopMovingRightCommand);
//        processor.onComplete();
//
//        StepVerifier.create(processor)
//                .assertNext(getGreetingAssertion(SESSION_ID)).as("check greeting")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isMovingUp());
//                })
//                .as("check user move up command")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isMovingLeft());
//                })
//                .as("check user move left command")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isStopMovingUp());
//                })
//                .as("check user stop move up command")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isMovingUp());
//                })
//                .as("check user move up command")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isStopMovingUp());
//                })
//                .as("check user stop move up command")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isStopMovingLeft());
//                })
//                .as("check user stop move left command")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isMovingRight());
//                })
//                .as("check user move right command")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isMovingDown());
//                })
//                .as("check user move down command")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isStopMovingDown());
//                })
//                .as("check user stop move down command")
//                .assertNext(worldChange -> {
//                    System.out.println(worldChange);
//                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
//                    assertTrue(worldChange.getUser().isStopMovingRight());
//                })
//                .as("check user stop move right command")
//                .expectComplete()
//                .log()
//                .verify(TIMEOUT);
//    }
}
