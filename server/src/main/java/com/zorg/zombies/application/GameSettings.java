package com.zorg.zombies.application;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("game")
public class GameSettings {

    @Setter
    @Getter
    private int humanWalkDelayMs;

}
