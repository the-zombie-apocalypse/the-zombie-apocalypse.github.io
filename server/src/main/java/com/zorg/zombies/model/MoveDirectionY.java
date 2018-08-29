package com.zorg.zombies.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MoveDirectionY implements MoveDirection {
    @JsonProperty("north") NORTH,
    @JsonProperty("south") SOUTH
}
