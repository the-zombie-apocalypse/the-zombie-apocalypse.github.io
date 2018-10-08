package com.zorg.zombies.map;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserPositionChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.change.WorldOnLoad;
import com.zorg.zombies.model.Coordinates;
import com.zorg.zombies.model.UserSubscriber;
import com.zorg.zombies.service.UsersCommunicator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.FluxProcessor;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultMapChunkTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    @Test
    void notifyUsers_When_NoUsersSubscribedYet_Expect_SilentConsumingByChunk() {
        final DefaultMapChunk chunk = new DefaultMapChunk();

        chunk.notifyUsers(WorldOnLoad.forTest("id", new Coordinates()));
    }

    @Test
    void publishCheck_When_OneSubscriberAndOneChangePublished_Expect_Received() {
        final DefaultMapChunk chunk = new DefaultMapChunk();
        final String id0 = "id-0";

        final UsersCommunicator usersCommunicator = mock(UsersCommunicator.class);
        final UserSubscriber user = new UserSubscriber(id0, new Coordinates(), usersCommunicator);

        chunk.addObject(user);

        final Coordinates newCoordinates = new Coordinates(42, 24);
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id0, newCoordinates)));

        chunk.unregister(id0);

        final FluxProcessor<WorldChange, WorldChange> processor0 = user.getSubscriber();
        processor0.onComplete();

        StepVerifier.create(processor0).as("proc-0 connection...")
                .assertNext(worldChange -> verifyChange(id0, newCoordinates, worldChange))
                .as("Checking user id and coordinates...")
                .expectComplete()
                .log()
                .verify(TIMEOUT);
    }

    @Test
    void publishCheck_When_FewConsumersAndFewChanges_Expect_OnlyCorrectChangesReceived() {
        final DefaultMapChunk chunk = new DefaultMapChunk();
        final String id0 = "id-0";
        final String id1 = "id-1";
        final String id2 = "id-2";

        final UsersCommunicator usersCommunicator = mock(UsersCommunicator.class);
        final UserSubscriber user0 = new UserSubscriber(id0, new Coordinates(), usersCommunicator);
        chunk.addObject(user0);

        final Coordinates coordinates0 = new Coordinates(42, 24);
        final Coordinates coordinates1 = new Coordinates(43, 23);
        final Coordinates coordinates2 = new Coordinates(44, 24);
        final Coordinates coordinates3 = new Coordinates(43, 23);
        final Coordinates coordinates4 = new Coordinates(43, 22);
        final Coordinates coordinates5 = new Coordinates(42, 21);
        final Coordinates coordinates6 = new Coordinates(41, 20);
        final Coordinates coordinates7 = new Coordinates(40, 20);

        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id0, coordinates0)));
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id0, coordinates2)));

        final UserSubscriber user1 = new UserSubscriber(id1, new Coordinates(), usersCommunicator);
        chunk.addObject(user1);

        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id1, coordinates1)));
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id1, coordinates4)));

        final UserSubscriber user2 = new UserSubscriber(id2, new Coordinates(), usersCommunicator);
        chunk.addObject(user2);

        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id2, coordinates3)));
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id0, coordinates5)));
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id2, coordinates6)));
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id1, coordinates7)));

        chunk.unregister(id0);
        chunk.unregister(id1);
        chunk.unregister(id2);

        final FluxProcessor<WorldChange, WorldChange> processor0 = user0.getSubscriber();
        final FluxProcessor<WorldChange, WorldChange> processor1 = user1.getSubscriber();
        final FluxProcessor<WorldChange, WorldChange> processor2 = user2.getSubscriber();

        processor0.onComplete();
        processor1.onComplete();
        processor2.onComplete();

        StepVerifier.create(processor0).as("proc-0 connection...")
                .assertNext(worldChange -> verifyChange(id0, coordinates0, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id0, coordinates2, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id1, coordinates1, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id1, coordinates4, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id2, coordinates3, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id0, coordinates5, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id2, coordinates6, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id1, coordinates7, worldChange))
                .as("Checking has been notified correctly")
                .expectComplete()
                .log()
                .verify(TIMEOUT);

        StepVerifier.create(processor1).as("proc-1 connection...")
                .assertNext(worldChange -> verifyChange(id1, coordinates1, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id1, coordinates4, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id2, coordinates3, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id0, coordinates5, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id2, coordinates6, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id1, coordinates7, worldChange))
                .as("Checking has been notified correctly")
                .expectComplete()
                .log()
                .verify(TIMEOUT);

        StepVerifier.create(processor2).as("proc-2 connection...")
                .assertNext(worldChange -> verifyChange(id2, coordinates3, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id0, coordinates5, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id2, coordinates6, worldChange))
                .as("Checking has been notified correctly")
                .assertNext(worldChange -> verifyChange(id1, coordinates7, worldChange))
                .as("Checking has been notified correctly")
                .expectComplete()
                .log()
                .verify(TIMEOUT);
    }

    private void verifyChange(String userId, Coordinates coordinates, WorldChange worldChange) {
        final UserChange userChange = worldChange.getUser();

        assertEquals(userId, userChange.getId());
        assertTrue(userChange instanceof UserPositionChange);

        final UserPositionChange userPositionChange = (UserPositionChange) userChange;

        assertEquals(coordinates, userPositionChange.getCoordinates());
    }
}