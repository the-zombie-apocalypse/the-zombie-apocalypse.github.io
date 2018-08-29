package com.zorg.zombies.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MoveDirectionX implements MoveDirection {
    @JsonProperty("east") EAST,
    @JsonProperty("west") WEST
}
