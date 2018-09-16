package com.zorg.zombies.model;

import lombok.Data;

@Data
public class UserData {

    protected String id;
    protected Coordinates coordinates;

    public UserData(String id, Coordinates coordinates) {
        this.id = id;
        this.coordinates = coordinates;
    }
}
