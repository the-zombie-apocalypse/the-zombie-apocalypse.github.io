package com.zorg.zombies.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MoveDirectionZ implements MoveDirection {
    @JsonProperty("up") UP,
    @JsonProperty("down") DOWN
}
