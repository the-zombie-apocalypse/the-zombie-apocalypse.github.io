package com.zorg.zombies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.factory.CommandFactory;
import com.zorg.zombies.service.GameSupervisor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@RequiredArgsConstructor
public class GameWebSocketHandler implements WebSocketHandler {

    private final CommandFactory commandFactory;
    private final GameSupervisor gameSupervisor;
    private final ObjectMapper mapper;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        FluxProcessor<Command, WorldChange> processor = gameSupervisor.createGameActionsProcessor(session.getId());

        Mono<Void> webSocketMessage = session.receive()
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

        Mono<Void> send = session.send(processor.map(
                worldChange -> session.textMessage(toJson(worldChange))
        ));

        return Mono.zip(webSocketMessage, send).then();
    }

    private void onError(Throwable throwable) {
        log.error("Socket handler received error", throwable);
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
