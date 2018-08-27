package com.zorg.zombies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Collections;

@SpringBootApplication
public class ZombiesApplication {

    public static final String ENTRY = "/conn";

    public static void main(String[] args) {
        SpringApplication.run(ZombiesApplication.class, args);
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public HandlerMapping handlerMapping(GameWebSocketHandler gameWebSocketHandler) {
        final SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setUrlMap(Collections.singletonMap(ENTRY, gameWebSocketHandler));
        handlerMapping.setOrder(1);
        return handlerMapping;
    }

}
