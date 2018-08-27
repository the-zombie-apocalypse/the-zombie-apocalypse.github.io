package com.zorg.zombies;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.ReplayProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameSupervisor {

    private final UserIdDefiner userIdDefiner;

    private Map<String, FluxProcessor<Command, WorldChange>> userIdToProcessor = new ConcurrentHashMap<>();

    @Autowired
    public GameSupervisor(UserIdDefiner userIdDefiner) {
        this.userIdDefiner = userIdDefiner;
    }

    public FluxProcessor<Command, WorldChange> createGameActionsProcessor(String sessionId) {
        final String id = userIdDefiner.getUserId(sessionId);
        final User user = new User(id);

        final GameActionsProcessor processor = new GameActionsProcessor(user);
        userIdToProcessor.put(user.getId(), processor);

        processor.doOnComplete(() -> userIdToProcessor.remove(id)).subscribe();

        return processor;
    }

    private void hookOnNext(Command command, Subscriber<WorldChange> changes) {
        final User user = command.getUser();

        if (user != null) {

            if (user.isMovingUp()) {
                // todo: implement checking is move legal
                final WorldChange worldChange = new WorldChange(new User(user.getId()));
                worldChange.getUser().setMovingUp(true);

                changes.onNext(worldChange);
            }
            if (user.isStopMovingUp()) {
                final WorldChange worldChange = new WorldChange(new User(user.getId()));
                worldChange.getUser().setStopMovingUp(true);

                changes.onNext(worldChange);
            }

            if (user.isMovingDown()) {
                // todo: implement checking is move legal
                final WorldChange worldChange = new WorldChange(new User(user.getId()));
                worldChange.getUser().setMovingDown(true);

                changes.onNext(worldChange);
            }
            if (user.isStopMovingDown()) {
                final WorldChange worldChange = new WorldChange(new User(user.getId()));
                worldChange.getUser().setStopMovingDown(true);

                changes.onNext(worldChange);
            }

            if (user.isMovingLeft()) {
                // todo: implement checking is move legal
                final WorldChange worldChange = new WorldChange(new User(user.getId()));
                worldChange.getUser().setMovingLeft(true);

                changes.onNext(worldChange);
            }
            if (user.isStopMovingLeft()) {
                final WorldChange worldChange = new WorldChange(new User(user.getId()));
                worldChange.getUser().setStopMovingLeft(true);

                changes.onNext(worldChange);
            }

            if (user.isMovingRight()) {
                // todo: implement checking is move legal
                final WorldChange worldChange = new WorldChange(new User(user.getId()));
                worldChange.getUser().setMovingRight(true);

                changes.onNext(worldChange);
            }
            if (user.isStopMovingRight()) {
                final WorldChange worldChange = new WorldChange(new User(user.getId()));
                worldChange.getUser().setStopMovingRight(true);

                changes.onNext(worldChange);
            }
        }
    }

    class GameActionsProcessor extends FluxProcessor<Command, WorldChange> {

        final User user;
        final ReplayProcessor<WorldChange> changes = ReplayProcessor.create(256);

        GameActionsProcessor(User user) {
            this.user = user;
            this.changes.onNext(new WorldOnLoad(user));
        }

        @Override
        public void onSubscribe(Subscription s) {
            System.out.println("onSubscribe: " + s);
        }

        @Override
        public void onNext(Command command) {
            System.out.println("onNext: " + command);
            hookOnNext(command, changes);
        }

        @Override
        public void onError(Throwable t) {
            System.out.println("onError: " + t);
        }

        @Override
        public void onComplete() {
            System.out.println("onComplete");
            changes.onComplete();
        }

        @Override
        public void subscribe(CoreSubscriber<? super WorldChange> actual) {
            System.out.println("subscribe: " + actual);
            changes.subscribe(actual);
        }
    }
}
