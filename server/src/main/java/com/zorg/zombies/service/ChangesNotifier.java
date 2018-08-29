package com.zorg.zombies.service;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.WorldChange;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChangesNotifier {

    private final Map<String, GameActionsProcessor> userIdToProcessor = new ConcurrentHashMap<>();

    public void register(String userId, GameActionsProcessor processor) {
        userIdToProcessor.put(userId, processor);
    }

    public void remove(String userId) {
        userIdToProcessor.remove(userId);
    }

    public void notifyUserUpdate(UserChange userChange) {
        val processor = userIdToProcessor.get(userChange.getId());

        if (processor != null) {
            val change = new WorldChange(userChange);
            processor.getSubscriber().onNext(change);
        }
    }
}
