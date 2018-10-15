package com.zorg.zombies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.factory.CommandFactory;
import com.zorg.zombies.service.GameSupervisor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.Mono;

@Component
public class GameWebSocketHandler implements WebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(GameWebSocketHandler.class);

    private final CommandFactory commandFactory;
    private final GameSupervisor gameSupervisor;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public GameWebSocketHandler(CommandFactory commandFactory, GameSupervisor gameSupervisor) {
        this.commandFactory = commandFactory;
        this.gameSupervisor = gameSupervisor;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        final FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(session.getId());

        final Mono<Void> webSocketMessage = session.receive()
                .map(this::webSocketMessageToCommand)
                .doOnError(this::onError)
                .doOnNext(processor::onNext)
                .doOnError(this::onError)
                .doOnComplete(() -> {
                    session.close();
                    processor.onComplete();
                })
                .log()
                .then();

        final Mono<Void> send = session.send(processor.map(
                worldChange -> session.textMessage(toJson(worldChange))
        ));

        return Mono.zip(webSocketMessage, send).then();
    }

    private void onError(Throwable throwable) {
        logger.error("Error : " + throwable);
        throwable.printStackTrace();
    }

    @SneakyThrows
    private Command webSocketMessageToCommand(WebSocketMessage webSocketMessage) {
        return commandFactory.fromJson(webSocketMessage.getPayloadAsText());
    }

    @SneakyThrows
    private String toJson(Object object) {
        return mapper.writeValueAsString(object);
    }

}
