package com.zorg.zombies;

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

    @Test
    void reactionOnCommand_When_UserStartMovingUp_Expect_UserMovingUpResponse() {
        final Command userMovingUpCommand = new Command();
        final User user = new User(SESSION_ID);
        user.setMovingUp(true);
        userMovingUpCommand.setUser(user);

        final FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(SESSION_ID);
        processor.onNext(userMovingUpCommand);
        processor.onComplete();

        StepVerifier.create(processor)
                .assertNext(getGreetingAssertion(SESSION_ID)).as("check greeting")
                .assertNext(worldChange -> {
                    System.out.println(worldChange);
                    assertEquals(worldChange.getUser().getId(), SESSION_ID);
                    assertTrue(worldChange.getUser().isMovingUp());
                })
                .as("check user move command")
                .expectComplete()
                .log()
                .verify(TIMEOUT);
    }
}
