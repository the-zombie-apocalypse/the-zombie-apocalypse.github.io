package com.zorg.zombies;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.Mono;

@Component
public class GameWebSocketHandler implements WebSocketHandler {

    private final GameSupervisor gameSupervisor;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public GameWebSocketHandler(GameSupervisor gameSupervisor) {
        this.gameSupervisor = gameSupervisor;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        final FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(session.getId());

        final Mono<Void> webSocketMessage = session.receive().log()
                .map(this::webSocketMessageToCommand)
                .doOnNext(processor::onNext)
                .then();

        final Mono<Void> send = session.send(processor.map(
                worldChange -> session.textMessage(toJson(worldChange))
        ));

        return webSocketMessage.and(send).then();
    }

    @SneakyThrows
    private Command webSocketMessageToCommand(WebSocketMessage webSocketMessage) {
        return mapper.readValue(webSocketMessage.getPayloadAsText(), Command.class);
    }

    @SneakyThrows
    private String toJson(Object object) {
        return mapper.writeValueAsString(object);
    }

}
