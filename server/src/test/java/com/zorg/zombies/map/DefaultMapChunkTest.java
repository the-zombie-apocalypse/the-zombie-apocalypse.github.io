package com.zorg.zombies.map;

import com.zorg.zombies.change.OnLoadUserChange;
import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserPositionChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.change.WorldOnLoad;
import com.zorg.zombies.model.Coordinates;
import com.zorg.zombies.model.User;
import com.zorg.zombies.service.UsersCommunicator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.FluxProcessor;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DefaultMapChunkTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    private UsersCommunicator communicator = Mockito.mock(UsersCommunicator.class);

    @Test
    void notifyUsers_When_NoUsersSubscribedYet_Expect_SilentConsumingByChunk() {
        DefaultMapChunk chunk = new DefaultMapChunk();

        chunk.notifyUsers(new WorldOnLoad(
                new OnLoadUserChange("id", "nickname", new Coordinates()), new ArrayList<>()
        ));
    }

    @Test
    void publishCheck_When_OneSubscriberAndOneChangePublished_Expect_Received() {
        DefaultMapChunk chunk = new DefaultMapChunk();
        String id0 = "id-0";

        var user = new User(id0, communicator);

        chunk.addObject(user);

        Coordinates newCoordinates = new Coordinates(42, 24);
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id0, newCoordinates)));

        FluxProcessor<WorldChange, WorldChange> processor0 = user.getSubscriber();
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
        DefaultMapChunk chunk = new DefaultMapChunk();
        String id0 = "id-0";
        String id1 = "id-1";
        String id2 = "id-2";

        var user0 = new User(id0, communicator);
        chunk.addObject(user0);

        Coordinates coordinates0 = new Coordinates(42, 24);
        Coordinates coordinates1 = new Coordinates(43, 23);
        Coordinates coordinates2 = new Coordinates(44, 24);
        Coordinates coordinates3 = new Coordinates(43, 23);
        Coordinates coordinates4 = new Coordinates(43, 22);
        Coordinates coordinates5 = new Coordinates(42, 21);
        Coordinates coordinates6 = new Coordinates(41, 20);
        Coordinates coordinates7 = new Coordinates(40, 20);

        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id0, coordinates0)));
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id0, coordinates2)));

        var user1 = new User(id1, communicator);
        chunk.addObject(user1);

        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id1, coordinates1)));
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id1, coordinates4)));

        var user2 = new User(id2, communicator);
        chunk.addObject(user2);

        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id2, coordinates3)));
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id0, coordinates5)));
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id2, coordinates6)));
        chunk.notifyUsers(new WorldChange<>(new UserPositionChange(id1, coordinates7)));

        FluxProcessor<WorldChange, WorldChange> processor0 = user0.getSubscriber();
        FluxProcessor<WorldChange, WorldChange> processor1 = user1.getSubscriber();
        FluxProcessor<WorldChange, WorldChange> processor2 = user2.getSubscriber();

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
        UserChange userChange = worldChange.getUser();

        assertEquals(userId, userChange.getId());
        assertTrue(userChange instanceof UserPositionChange);

        UserPositionChange userPositionChange = (UserPositionChange) userChange;

        assertEquals(coordinates, userPositionChange.getCoordinates());
    }
}
