package com.zorg.zombies.service;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.WorldChange;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.ReplayProcessor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ChangesNotifierTest {

    @InjectMocks
    private ChangesNotifier changesNotifier;

    @Test
    void register() {
        val processor = mock(GameActionsProcessor.class);
        val id = "id";
        changesNotifier.register(id, processor);

        assertEquals(processor, changesNotifier.userIdToProcessor.get(id));
    }

    @Test
    void remove() {
        val processor = mock(GameActionsProcessor.class);
        val id = "id";

        changesNotifier.userIdToProcessor.put(id, processor);

        changesNotifier.remove(id);

        assertNull(changesNotifier.userIdToProcessor.get(id));
    }

    @Test
    void notifyUserUpdate() {
        val id = "id";
        val processor = mock(GameActionsProcessor.class);
        val userChange = new UserChange(id);
        val worldChange = new WorldChange(userChange);
        final ReplayProcessor<WorldChange> subscriber = ReplayProcessor.create();

        given(processor.getSubscriber()).willReturn(subscriber);

        changesNotifier.register(id, processor);

        changesNotifier.notifyUserUpdate(userChange);

        val worldChanges = subscriber.toIterable().iterator();

        assertTrue(worldChanges.hasNext());

        final WorldChange next = worldChanges.next();

        assertEquals(worldChange, next);
    }
}
